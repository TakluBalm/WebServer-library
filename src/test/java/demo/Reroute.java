package demo;

import server.Controller;
import server.MethodHandler;
import server.Request;
import server.Response;

@Controller(URL = "/")
public class Reroute {
	@MethodHandler(method = "GET")
	public Response handler(Request request){
		Response r = new Response("1.1").setStatusCode(301).setHeader("Location", "/home");
		System.out.println(r);
		return r;
	}
}
