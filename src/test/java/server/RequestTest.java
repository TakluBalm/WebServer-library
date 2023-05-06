package server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import server.Request;
import server.Route;

public class RequestTest {
    @Test
    void testRequestAsForm() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/x-www-form-urlencoded");
        Request r = new Request(new Route("GET","/random"), headers, null, null, "abc=123&bcd=234".getBytes());
        Map<String, String> form = r.requestAsForm();
        assertEquals(form.get("abc"), "123");
        assertEquals(form.get("bcd"), "234");
    }
}
