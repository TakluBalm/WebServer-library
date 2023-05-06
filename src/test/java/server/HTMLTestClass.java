package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.Controller;
import server.HTMLResponse;
import server.MethodHandler;
import server.Request;
import server.Resource;
import server.Response;


@Controller(URL="/test/html")
public class HTMLTestClass {
    @MethodHandler(method = "GET")
    public Response htmlSender(Request request) throws Exception{
        HTMLResponse r = new HTMLResponse("1.1", "src/test/resources/test_1_response.html");
        HashMap<String, Resource> mp = new HashMap<>();
		List<String> bruh = new ArrayList<>();
		bruh.add("Jugal");
		bruh.add("Prakhar");
		bruh.add("Tanuj");
		List<String> lmao = new ArrayList<>();
		lmao.add("Gautam");
		lmao.add("Vinay");
		lmao.add("Pajji");
		mp.put("bruh", new Resource("iterable").loadData(bruh));
		mp.put("name", new Resource("text").loadData("JPT"));
		mp.put("lmao", new Resource("iterable").loadData(lmao));
		mp.put("URL", new Resource("text").loadData("/prakhar"));
        r.embedData(mp);
        return r;
    }
}
