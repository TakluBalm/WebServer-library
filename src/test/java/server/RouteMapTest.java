package server;

import org.junit.jupiter.api.Test;

import server.Invocation;
import server.Route;
import server.RouteMap;

import static org.junit.jupiter.api.Assertions.*;

class RouteMapTest {

    @Test
    void putEntryAndRetrieve() throws NoSuchMethodException {
        RouteMap r = new RouteMap();
        Route route = new Route("GET", "/tanuj");
        Invocation invocation = new Invocation(new Object(), this.getClass().getDeclaredMethod("putEntryAndRetrieve"), "");
        r.put(route, invocation);
        System.out.println(r.keySet());
        assertEquals(r.get(route), invocation);
    }

    @Test
    void putVariableEntryAndCheck() throws NoSuchMethodException{
        RouteMap r = new RouteMap();
        Route route = new Route("GET", "/tanuj/{id}");
        Invocation invocation = new Invocation(new Object(), this.getClass().getDeclaredMethod("putVariableEntryAndCheck"), "");
        r.put(route, invocation);
        System.out.println(r.keySet());
        Route test1 = new Route("GET", "/tanuj/123");
        Route test2 = new Route("PUT", "/tanuj/123");
        assertEquals(r.get(test1), invocation);
        assertEquals(r.get(test2), null);
    }
}