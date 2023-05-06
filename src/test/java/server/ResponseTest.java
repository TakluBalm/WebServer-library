package server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import server.Response;

public class ResponseTest {
	@Test
	public void testAddHeader() {
		Response r = new Response("1.1");
		r.setHeader("tanuj-bhai", "katai harami");
		String testVal = r.getHeaderValue("Tanuj-bhai");
		assertEquals(testVal, "katai harami");
	}

	@Test
	public void testSetCookie() {
		Response r = new Response("1.1");
		String value = r.setCookie("System", "69").getCookie("System");
		assertEquals("69",value);
	}

	@Test
	public void testSetStatusCode() {
		Response r = new Response("1.1");
		r.setStatusCode(420);
		assertEquals(r.getStatusCode(), 420);
	}

	@Test
	public void testToString() {
		Response r = new Response("1.1");
//		r.setBody("Hello Guys").setCookie("session-id", "1234").setHeader("Content-type", "text/text").setStatusCode(404).setCookie("test", "semicolon");
		System.out.println(r.toString());
	}
}