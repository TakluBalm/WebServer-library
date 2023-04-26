package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class HTTPRequestTest {
    @Test
    public void objectTest(){
        String req = "GET /example HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Connection: keep-alive\r\n" +
                "Cookie: sessionId=1234abcd; userId=5678efgh\r\n";

        HTTPRequest request = new HTTPRequest(req);

        assertEquals (request.getCookies().get(0), "sessionId=1234abcd");
        assertEquals (request.getCookies().get(1), "userId=5678efgh");
        assertEquals (request.getMethod(), "GET");
        assertEquals (request.getPath(), "/example");
        assertEquals (request.getVersion(), "HTTP/1.1");
    }
}