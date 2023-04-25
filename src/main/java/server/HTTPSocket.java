package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HTTPSocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public HTTPSocket(Socket serversocket){
        socket = serversocket;
        try {
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
        private String method;
        private String path;
        private String version;
        private Map<String, String> headers;
        private List<String> cookies;
        private String body;

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
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
            builder.append(method).append(" ").append(path).append(" ").append(version).append("\n");
            for (String key : headers.keySet()) {
                builder.append(key).append(": ").append(headers.get(key)).append("\n");
            }
            builder.append("\n");
            builder.append(body);
            return builder.toString();
        }

        public Request() throws IOException {
            // Parse request line
            try {
                String[] requestLine = (reader.readLine()).split(" ");
                method = requestLine[0];
                path = requestLine[1];
                version = requestLine[2];

                // Parse headers
                headers = new HashMap<>();
                cookies = new ArrayList<>();

                String line;
                while ((line = reader.readLine()).length() > 0) {
                    String[] header = line.split(": ");
                    String headerName = header[0];
                    String headerValue = header[1];
                    headers.put(headerName, headerValue);
                    if (headerName.equalsIgnoreCase("cookie")) {
                        cookies.addAll(Arrays.asList(headerValue.split("; ")));
                    }
                }

                // Parse body
                body = "";
                String contentLen = headers.get("Content-length");
                int bodyLen = (contentLen != null) ? Integer.parseInt(contentLen) : 0;
                StringBuilder bodyBuilder = new StringBuilder();
                for (int i = 0; i < bodyLen; i++) {
                    bodyBuilder.append(reader.readLine());
                }
                body = bodyBuilder.toString();
            } catch (Exception e){
                System.out.println(e);
                throw e;
            }
        }

    }

    public Request waitRequest(){
        try {
            return new Request();
        } catch (IOException e) {
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
