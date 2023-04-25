package server;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPRequest {
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

    public HTTPRequest(String requestString) {
        String[] lines = requestString.split("\r\n");

        // Parse request line
        String[] requestLine = lines[0].split(" ");
        method = requestLine[0];
        path = requestLine[1];
        version = requestLine[2];

        // Parse headers
        headers = new HashMap<>();
        cookies = new ArrayList<>();

        int i = 1;
        while (i < lines.length && lines[i].length() > 0) {
            String[] header = lines[i].split(": ");
            String headerName = header[0];
            String headerValue = header[1];
            headers.put(headerName, headerValue);
            if (headerName.equalsIgnoreCase("cookie")) {
                cookies.addAll(Arrays.asList(headerValue.split("; ")));
            }
            i++;
        }

        // Parse body
        body = "";
        if (i < lines.length) {
            StringBuilder bodyBuilder = new StringBuilder();
            for (int j = i + 1; j < lines.length; j++) {
                bodyBuilder.append(lines[j]);
            }
            body = bodyBuilder.toString();
        }
    }

}
