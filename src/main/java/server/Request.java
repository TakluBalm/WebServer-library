package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
	protected Route route = new Route("GET", "/");
	protected String version = "HTTP/1.1";
	protected Map<String, String> headers = new HashMap<>();
	protected Map<String, String> params = new HashMap<>();
	protected List<String> cookies = new ArrayList<>();
	protected String body = "";

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

	public String getParameterValue(String parameter){
		return params.get(parameter);
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

	public Request(Route route, String version, Map<String, String> headers, List<String> cookies, Map<String, String> params, String body){
		this.route = route;
		this.version = version;
		this.body = body;
		this.cookies = cookies;
		this.headers = headers;
		this.params = params;
	}

}
