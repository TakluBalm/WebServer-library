package demo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;
import server.Request;
import server.Response;

@Controller(URL = "/html/{f}")
public class Index {
	
	@MethodHandler(method = "GET")
	public Response handler(Request request) throws Exception{

		System.out.println("Index.handler function called");

		String fname = "src/test/java/demo/"+request.getParameterValue("f")+".html";
		try(FileReader f = new FileReader(fname)){
			return new HTMLResponse("1.1", fname).embedData(new HashMap<>()); 	
		}catch(FileNotFoundException e){
			return new Response("1.1").setStatusCode(404).setBody("Hello".getBytes());
		}
	}
}
