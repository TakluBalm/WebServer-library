package server;

import java.util.HashMap;
import java.util.Set;

public class Response {
	private String version = "1.1";
	private int statusCode = 200;
	private HashMap<String, String> headers = new HashMap<>();
	private HashMap<String, String> cookies = new HashMap<>();
	private byte[] body;
	int cookie_cnt = 0;

	private static HashMap<Integer, String> statusMessages = new HashMap<>();

	static{
		statusMessages.put(69, "System");
		statusMessages.put(100, "Continue");
		statusMessages.put(101, "Switching Protocols");
		statusMessages.put(200, "OK");
		statusMessages.put(201, "Created");
		statusMessages.put(202, "Accepted");
		statusMessages.put(203, "Non-Authoritative Information");
		statusMessages.put(204, "No Content");
		statusMessages.put(205, "Reset Content");
		statusMessages.put(206, "Partial Content");
		statusMessages.put(300, "Multiple Choices");
		statusMessages.put(301, "Moved Permanently");
		statusMessages.put(302, "Found");
		statusMessages.put(303, "See Other");
		statusMessages.put(304, "Not Modified");
		statusMessages.put(305, "Use Proxy");
		statusMessages.put(307, "Temporary Redirect");
		statusMessages.put(400, "Bad Request");
		statusMessages.put(401, "Unauthorized");
		statusMessages.put(402, "Payment Required");
		statusMessages.put(403, "Forbidden");
		statusMessages.put(404, "Not Found");
		statusMessages.put(405, "Method Not Allowed");
		statusMessages.put(406, "Not Acceptable");
		statusMessages.put(407, "Proxy Authentication Required");
		statusMessages.put(408, "Request Timeout");
		statusMessages.put(409, "Conflict");
		statusMessages.put(410, "Gone");
		statusMessages.put(411, "Length Required");
		statusMessages.put(412, "Precondition Failed");
		statusMessages.put(413, "Payload Too Large");
		statusMessages.put(414, "URI Too Long");
		statusMessages.put(415, "Unsupported Media Type");
		statusMessages.put(416, "Range Not Satisfiable");
		statusMessages.put(417, "Expectation Failed");
		statusMessages.put(500, "Internal Server Error");
		statusMessages.put(501, "Not Implemented");
		statusMessages.put(502, "Bad Gateway");
		statusMessages.put(503, "Service Unavailable");
		statusMessages.put(504, "Gateway Timeout");
		statusMessages.put(505, "HTTP Version Not Supported");

	}

	public Response(String version) {
		this.version = version;
	}

	public Response setHeader(String field, String value){
		this.headers.put(field.toLowerCase(), value);
		return this;
	}

	public String getHeaderValue(String header_field){
		return headers.get(header_field.toLowerCase());
	}
	public String getCookie(String cookie_field)
	{
		return cookies.get(cookie_field);
	}

	public int getStatusCode(){
		return statusCode;
	}

	public Response setStatusCode(int code){
		this.statusCode = code;
		return this;
	}

	public Response setCookie(String cookieName, String cookieValue){
		this.cookies.put(cookieName, cookieValue);
		cookie_cnt++;
		return this;
	}

	public Response setBody(byte[] body){
		this.body = body;
		return this;
	}

	public String headerString() {
		StringBuilder msgBuilder = new StringBuilder();

		//	Content-length System
		int contentLen = (body != null)?body.length:0;
		if(contentLen > 0){
			String setLen = headers.get("content-length");
			if(setLen == null || Integer.parseInt(setLen) != contentLen){
				headers.put("content-length", contentLen+"");
			}
		}

		//	Response Status
		msgBuilder.append("HTTP/").append(version).append(" ").append(statusCode).append(" ").append(statusMessages.get(statusCode)).append("\r\n");

		//	Header System
		Set<String> fields = headers.keySet();
		for(String field: fields){
			msgBuilder.append(field).append(": ").append(headers.get(field)).append("\r\n");
		}

		// Cookie System
		Set<String> cookieNames = cookies.keySet();
		if(cookieNames.size() > 0){
			int cnt = 1;
			msgBuilder.append("set-cookie: ");
			for(String cookieName: cookieNames){
				msgBuilder.append(cookieName).append("=").append(cookies.get(cookieName));
				if(cnt != cookie_cnt){
					msgBuilder.append("; ");
				}
				cnt++;
			}
			msgBuilder.append("\r\n");
		}
		return msgBuilder.toString();
	}

	public byte[] getBody() {
		return body;
	}
}
