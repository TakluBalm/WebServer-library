package server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
	protected Route route;
	protected String version = "HTTP/1.1";
	protected Map<String, String> headers = new HashMap<>();
	protected Map<String, String> params = new HashMap<>();
	protected List<String> cookies = new ArrayList<>();
	protected byte[] body;

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

	public byte[] getBody() {
		return body;
	}

	public String getCharset(){
		if (this.getHeaders().get("content-type") == null){
			return null;
		}
		String[] vals = this.getHeaders().get("content-type").strip().split(";");
		if(vals.length > 2){
			return null;
		}

		if(vals.length == 1){
			return "ISO-8859-1";
		}

		if(vals[1].strip().split("=").length != 2){
			return null;
		}

		if(vals[1].strip().split("=")[0].strip().equals("charset")){
			return vals[1].strip().split("=")[1].strip();
		}else{
			return "ISO-8859-1";
		}

		
	}

	public String getParameterValue(String parameter){
		return params.get(parameter);
	}

	public Map<String, String> requestAsForm(){
		String type = headers.get("content-type");
		if(type != null && type.contains("application/x-www-form-urlencoded")){
			try {
				String sbody = new String(body, "US-ASCII");
				String[] parameters = sbody.split("&");
				HashMap<String, String> mp = new HashMap<>();
				for(int i = 0; i < parameters.length; i++){
					String[] p = parameters[i].split("=");
					if(p.length != 2){
						return null;
					}
					mp.put(p[0].strip(), p[1].strip());
				}
				return mp;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
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

	public Request(
			Route route,
			Map<String, String> headers,
			List<String> cookies,
			Map<String, String> params,
			byte[] body
		){
		this.route = route;
		this.body = body;
		this.cookies = cookies;
		this.headers = headers;
		this.params = params;
	}

}
