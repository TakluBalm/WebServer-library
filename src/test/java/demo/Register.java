package demo;


import java.util.Map;

import ORM.Manager.Session;
import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;
import server.Request;
import server.Response;

@Controller(URL = "/register")
public class Register {

	@MethodHandler(method = "GET")
	public Response getRegisterPage(Request request){
		return new HTMLResponse("1.1", "src/test/java/demo/register.html");
	}

	@MethodHandler(method = "POST")
	public Response postRegisterPage(Request request) throws Exception{
		Session s = Main.sf.getSession();
		Users u = new Users();
		Map<String, String> form = request.requestAsForm();
		u.username = form.get("username");
		u.password = form.get("password");
		s.insert(u);
		s.commit();
		return new Response("1.1").setStatusCode(303).setHeader("Location", "/login");
	}
	
}
