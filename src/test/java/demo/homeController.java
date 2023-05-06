package demo;

import java.util.HashMap;

import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;
import server.Request;
import server.Response;

@Controller(URL="/home")
public class homeController {
	
	@MethodHandler(method = "GET")
	public HTMLResponse getHomepage(Request request){
		return new HTMLResponse("1.1", "src/test/java/demo/home.html");
	}
}
