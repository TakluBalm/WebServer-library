package server;

import java.io.IOException;

import server.Controller;
import server.MethodHandler;
import server.Request;
import server.Response;

@Controller(URL = "/tanuj/{id}")
public class JugalPrakharTanuj {

	@MethodHandler(method = "GET")
	public Response handler(Request r) throws IOException {
		return new Response("1.1").setCookie("set-cookie", "123");
	}
}