package server;

@Controller(URL = "/prakhar")
public class BabuMoshai {
    int cnt = 0;
    @MethodHandler(method = "GET")
    Response tp(Request request){
//        return new Response("1.1").setBody(request.getBody()).setCookie("json-got", "true");
        return new ImageResponse("1.1", "D:\\Downloads\\20230427_154036.jpeg")
//                .setHeader("cache-control", "max-age=3600")
//                .setHeader("content-disposition", "attachment; filename=\"20230427_154036.jpg\"")
        ;
    }
}