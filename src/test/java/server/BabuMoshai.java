package server;

@Controller(URL = "/prakhar")
public class BabuMoshai {
    public BabuMoshai(){

    }

    int cnt = 0;
    @MethodHandler(method = "GET")
    int tp(HTTPRequest request){
        System.out.println("runnning baby");
        System.out.println(cnt);
        cnt++;
        return cnt;
    }
}