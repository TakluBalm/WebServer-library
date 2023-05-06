package demo;

import server.Request;
import server.Resource;
import server.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ORM.Manager.Session;
import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;

@Controller(URL="/login")
public class Login {

	@MethodHandler(method = "POST")
	public Response postloginHandler(Request request) throws Exception{
		Session s = Main.sf.getSession();
		Users ut = new Users();
		Map<String, String> form = request.requestAsForm();
		ut.username = form.get("username");
		ut.password = form.get("password");

		List<Users> all = s.getAll(Users.class);
		boolean isLegit = false;

		for(Users u: all){
			System.out.println(u.username);
			if(u.username.equals(ut.username) && u.password.equals(ut.password)){
				isLegit = true;
				break;
			}
		}

		Map<String, Resource> context = new HashMap<>();
		context.put("USER", new Resource("text"));

		return isLegit ? (new Response("1.1").setStatusCode(303).setHeader("Location", "/user/"+ut.username)) : (new Response("1.1").setStatusCode(303).setHeader("Location", "/home"));

	}

	@MethodHandler(method = "GET")
	public Response getloginHandler(Request request) throws Exception{
		return new HTMLResponse("1.1", "src/test/java/demo/login.html");
	}
}
