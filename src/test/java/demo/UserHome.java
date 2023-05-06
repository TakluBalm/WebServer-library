package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;
import server.Request;
import server.Resource;
import server.Response;
import server.exceptions.InvalidResourceTypeException;

@Controller(URL = "/user/{name}")
public class UserHome {
	
	@MethodHandler(method = "GET")
	public Response getHandler(Request request) throws Exception{
		try{
			Map<String, Resource> context = new HashMap<>();
			context.put("name", new Resource("text").loadData(request.getParameterValue("name")));
			List<Object> l = new ArrayList<>();
			l.add(new Object());
			context.put("items", new Resource("iterable").loadData(l));
			System.out.println(context);
			return new HTMLResponse("1.1", "src/test/java/demo/UserHome.html").embedData(context);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	@MethodHandler(method = "POST")
	public Response postHandler(Request request) throws Exception{
		Map<String, Resource> context = new HashMap<>();
		context.put("name", new Resource("text").loadData(request.getParameterValue("name")));
		Map<String,String> form = request.requestAsForm();
		int number = Integer.parseInt(form.get("num"));
		List<Object> items = new ArrayList<>();
		for(int i = 0; i < number; i++){
			items.add(new Object());
		}
		context.put("items", new Resource("iterable").loadData(items));

		System.out.println(context);
		return new HTMLResponse("1.1", "src/test/java/demo/UserHome.html").embedData(context);
	}
}
