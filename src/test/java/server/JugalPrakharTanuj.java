package server;

@Controller(URL = "/tanuj/{id}")
public class JugalPrakharTanuj {

	@MethodHandler(method = "GET")
	Response handler(Request r, String id){
		return new Response("1.1").setCookie("id", id).setStatusCode(200);
	}
}