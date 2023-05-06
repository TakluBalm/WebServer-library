package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.Server;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    Server s = new Server();
	Socket ss;
	PrintWriter writer;
	BufferedReader reader;

    @BeforeEach
    void setUp() throws IOException {
        s.start();

        ss = new Socket("localhost", 8080);
        InputStream in = ss.getInputStream();
        OutputStream out = ss.getOutputStream();
        writer = new PrintWriter(out, true);
        reader = new BufferedReader(new InputStreamReader(in));
    }

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        s.stop();
		ss.close();
		writer.close();
		reader.close();
    }

    @Test
    void setupTest() throws IOException {
        String msg = "Hello from test class!";
        writer.println(msg);
        String response = reader.readLine();
        assertTrue(response.equals("HTTP/1.1 400 Bad Request"));
    }

	@Test
	void httpRequestTestNegative() throws Exception{
        String msg = "GET /example HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Connection: keep-alive\r\n" +
                "Cookie: sessionId=1234abcd; userId=5678efgh\r\n";

        writer.print(msg);
		writer.flush();
        String response = reader.readLine();
        assertTrue(response.equals("HTTP/1.1 408 Request Timeout"));
	}

	@Test
	void httpRequestPositiveRouteNegative() throws IOException{

        String msg =
				"POST /api/v1/users HTTP/1.1\r\n"+
						"Host: example.com\r\n"+
						"Content-Type: application/json\r\n"+
						"Content-Length: 52\r\n"+
						"\r\n"+
						"{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}";

        writer.print(msg);
		writer.flush();
        String response = reader.readLine();
		System.out.println("response = " + response);
        assertTrue(response.equals("HTTP/1.1 404 Not Found"));
	}

	@Test
	void routeTest() throws IOException{

        String msg =
				"GET /prakhar HTTP/1.1\r\n"+
						"Host: example.com\r\n"+
						"Content-Type: application/json\r\n"+
						"Content-Length: 0\r\n"+
						"\r\n";

        writer.print(msg);
		writer.flush();
		String response = reader.readLine();
		assertEquals("HTTP/1.1 200 OK", response);
	}

	@Test
	void pageNotFoundTest() throws IOException {

        String msg =
				"GET /nonExistent HTTP/1.1\r\n"+
						"Host: example.com\r\n"+
						"Content-Type: application/json\r\n"+
						"Content-Length: 52\r\n"+
						"\r\n"+
						"{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}";

        writer.print(msg);
		writer.flush();
        String response = reader.readLine();
        assertTrue(response.equals("HTTP/1.1 404 Not Found"));
	}

	@Test
	void variableRoutingTest() throws IOException{
		String msg =
				"GET /tanuj/123 HTTP/1.1\r\n"+
						"Host: example.com\r\n"+
						"Content-Type: application/json\r\n"+
						"Content-Length: 52\r\n"+
						"\r\n"+
						"{\"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}";

		writer.print(msg);
		writer.flush();
		String line1 = reader.readLine();
		String line2 = reader.readLine();
		System.out.println(line1);
		System.out.println(line2);
		assertEquals(line1, "HTTP/1.1 200 OK");
//		assertEquals(line2, "set-cookie: id=123");
	}
}