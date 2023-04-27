package server;

@Controller(URL = "/prakhar")
public class BabuMoshai {
    int cnt = 0;
    @MethodHandler(method = "GET")
    Response tp(Request request){
        return new Response("1.1").setBody("Prakhar recieved: "+request.getBody()).setCookie("json-got", "true");
    }
}