package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.zip.*;

import server.exceptions.HttpRequestTimeoutException;

public class HTTPSocket {
    private Socket socket;
    private PrintWriter writer;
	InputStream inputStream;
	OutputStream outputStream;

    public HTTPSocket(Socket serversocket, int timeout) throws SocketException, IOException{
        socket = serversocket;
		socket.setSoTimeout(timeout);
		inputStream = serversocket.getInputStream();
		outputStream = serversocket.getOutputStream();
		writer = new PrintWriter(outputStream, true);
    }

	private String readLine() throws IOException{
        StringBuilder sb = new StringBuilder();
        int c = inputStream.read();
        while (c != '\n') {
            if (c != '\r') {
                sb.append((char) c);
            }
            c = inputStream.read();
        }
        if (c == -1 && sb.length() == 0) {
            return null;
        }
        return sb.toString();
	}

	private String timedRead() throws HttpRequestTimeoutException, IOException{
		try{
			return readLine();
		}catch(SocketTimeoutException e){
			throw new HttpRequestTimeoutException("Timed Out while waiting for Request");
		}
	}

	void sendStatusCode(int code) throws IOException{
		sendResponse(new Response("1.1").setStatusCode(code));
	}

	public Request tryRequest() throws IOException {
		try {
			while(true){
				// Parse request line
				String[] requestLine = timedRead().split(" ");
				if(requestLine.length != 3){
					sendStatusCode(400);
					continue;
				}

				String method = requestLine[0];
				String[] uri = requestLine[1].split("\\?");
				if(uri.length > 2){
					sendStatusCode(400);
					continue;
				}

				Route route = new Route(method, uri[0]);
				if(!requestLine[2].equalsIgnoreCase("HTTP/1.1")){
					sendStatusCode(505);
					continue;
				}


				Map<String, String> params = new HashMap<>();
				boolean reset = false;
				if(uri.length == 2){
					String[] parameters = uri[1].split("&");
					for(int i = 0; i < parameters.length; i++){
						String[] p = parameters[i].split("=");
						if(p.length != 2){
							sendStatusCode(400);
							reset = true;
							break;
						}
						params.put(p[0], p[1]);
					}
				}
				if(reset)	continue;
				reset = false;



				// Parse headers
				Map<String, String> headers = new HashMap<>();
				List<String> cookies = new ArrayList<>();

				String line;
				while ((line = timedRead()).length() > 0) {
					String[] header = line.split(": ");
					if(header.length != 2){
						sendStatusCode(400);
						reset = true;
						break;
					}
					String headerName = header[0].toLowerCase();
					String headerValue = header[1];

					headers.put(headerName, headerValue);

					if (headerName.equalsIgnoreCase("cookie")) {
						cookies.addAll(Arrays.asList(headerValue.split("; ")));
					}
				}
				if(reset)	continue;
				reset = false;


				// Parse body
				String contentLen = headers.get("content-length");
				String contentEncoding = headers.get("content-encoding");
				int bodyLen = (contentLen != null) ? Integer.parseInt(contentLen) : 0;
				byte[] body = new byte[bodyLen];
				try{
					inputStream.read(body);
					body = decode(body, contentEncoding);
					if(body == null){
						sendStatusCode(400);
						continue;
					}
				}catch(SocketTimeoutException e){
					throw new HttpRequestTimeoutException("Request Timed Out");
				}

				return new Request(route, headers, cookies, params, body);
			}
		} catch (HttpRequestTimeoutException e){
			e.printStackTrace();
			// sendStatusCode(408);
			return null;
		} catch (IOException e){
			e.printStackTrace();
			throw e;
		}
    }

	private byte[] decode(byte[] body, String contentEncoding) {
		if(contentEncoding == null)	return body;

		ByteArrayInputStream bodyStream = new ByteArrayInputStream(body);
		switch(contentEncoding.toLowerCase()){
			case "gzip":{
				try (GZIPInputStream gzipStream = new GZIPInputStream(bodyStream)) {
					return gzipStream.readAllBytes();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			case "deflate":{
				try(InflaterInputStream inflaterStream = new InflaterInputStream(bodyStream)){
					return inflaterStream.readAllBytes();
				}catch(IOException e){
					e.printStackTrace();
					return null;
				}
			}
			case "compress":{
				try(InflaterInputStream inflaterStream = new InflaterInputStream(bodyStream, new Inflater(true))){
					return inflaterStream.readAllBytes();
				}catch(IOException e){
					e.printStackTrace();
					return null;
				}
			}
			default:{
				return body;
			}
		}
	}

	public void closeConnection() throws IOException {
        socket.close();
    }

    public void sendResponse(Response msg) throws IOException {
		writer.write(msg.headerString());

		// Header End System
		writer.write("\r\n");
		writer.flush();

		// Full body system
		byte[] body = msg.getBody();
		if(body != null && body.length > 0){
			outputStream.write(msg.getBody());
		}
    }
}
