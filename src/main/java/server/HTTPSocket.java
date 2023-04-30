package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

import server.exceptions.HttpInvalidException;
import server.exceptions.HttpRequestTimeoutException;

public class HTTPSocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
	InputStream inputStream;
	OutputStream outputStream;

    public HTTPSocket(Socket serversocket, int timeOut){
        socket = serversocket;
        try {
			serversocket.setSoTimeout(timeOut);
            inputStream = serversocket.getInputStream();
            outputStream = serversocket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

	public Request waitRequest() {
		// Parse request line
		try {
			String[] requestLine;
			while(true){
				try{
					requestLine = (reader.readLine()).split(" ");
					break;
				}catch (SocketTimeoutException se){
					continue;
				}
			}

			if(requestLine.length != 3){
				throw new HttpInvalidException("Invalid HTTP request");
			}

			String method = requestLine[0];
			String[] uri = requestLine[1].split("\\?");
			if(uri.length > 2){
				throw new HttpInvalidException("Invalid HTTP request");
			}

			Route route = new Route(method, uri[0]);
			String version = requestLine[2];

			Map<String, String> params = new HashMap<>();
			if(uri.length == 2){
				String[] parameters = uri[1].split("&");
				for(int i = 0; i < parameters.length; i++){
					String[] p = parameters[i].split("=");
					if(p.length != 2) throw new HttpInvalidException("Invalid Exception");
					params.put(p[0], p[1]);
				}
			}


			// Parse headers
			Map<String, String> headers = new HashMap<>();
			List<String> cookies = new ArrayList<>();

			String line;
			try{
				while ((line = reader.readLine()).length() > 0) {
					String[] header = line.split(": ");
					if(header.length != 2){
						throw new HttpInvalidException("Invalid HTTP request");
					}
					String headerName = header[0].toLowerCase();
					String headerValue = header[1];
					headers.put(headerName, headerValue);
					if (headerName.equalsIgnoreCase("cookie")) {
						cookies.addAll(Arrays.asList(headerValue.split("; ")));
					}
				}
			}catch(SocketTimeoutException e){
				throw new HttpRequestTimeoutException("Connection Timed Out while waiting for Request");
			}

			// Parse body
			String body = "";
			String contentLen = headers.get("Content-Length");
			int bodyLen = (contentLen != null) ? Integer.parseInt(contentLen) : 0;
			char[] charBuf = new char[bodyLen];
			try{
				reader.read(charBuf);
				body = new String(charBuf);
			}catch(SocketTimeoutException e){
				throw new HttpRequestTimeoutException("Connection Timed Out while waiting for Request");
			}

			return new Request(route, version, headers, cookies, params, body);
		} catch (Exception e){
			System.out.println(e);
			return null;
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
