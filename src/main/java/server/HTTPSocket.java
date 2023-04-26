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

    public HTTPSocket(Socket serversocket, int timeOut){
        socket = serversocket;
        try {
			serversocket.setSoTimeout(timeOut);
            InputStream inputStream = serversocket.getInputStream();
            OutputStream outputStream = serversocket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public class Request {
		private Route route;
        private String version;
        private Map<String, String> headers;
        private List<String> cookies;
        private String body;

        public String getMethod() {
            return route.Method;
        }

        public String getPath() {
            return route.Path;
        }

        public String getVersion() {
            return version;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public List<String> getCookies() {
            return cookies;
        }

        public String getBody() {
            return body;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(route.Method).append(" ").append(route.Path).append(" ").append(version).append("\n");
            for (String key : headers.keySet()) {
                builder.append(key).append(": ").append(headers.get(key)).append("\n");
            }
            builder.append("\n");
            builder.append(body);
            return builder.toString();
        }

        public Request() throws HttpInvalidException, HttpRequestTimeoutException, IOException {
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

                route = new Route(requestLine[1], requestLine[0]);
                version = requestLine[2];

                // Parse headers
                headers = new HashMap<>();
                cookies = new ArrayList<>();

                String line;
				try{
					while ((line = reader.readLine()).length() > 0) {
						String[] header = line.split(": ");
						if(header.length != 2){
							throw new HttpInvalidException("Invalid HTTP request");
						}
						String headerName = header[0];
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
                body = "";
                String contentLen = headers.get("Content-Length");
                int bodyLen = (contentLen != null) ? Integer.parseInt(contentLen) : 0;
				char[] charBuf = new char[bodyLen];
				try{
					reader.read(charBuf);
					body = new String(charBuf);
				}catch(SocketTimeoutException e){
					throw new HttpRequestTimeoutException("Connection Timed Out while waiting for Request");
				}
            } catch (IOException e){
                throw e;
            }
        }

    }

    public Request waitRequest(){
        try {
            return new Request();
        } catch (Exception e) {
            return null;
        }
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public void send(String msg){
        writer.println(msg);
    }
}
