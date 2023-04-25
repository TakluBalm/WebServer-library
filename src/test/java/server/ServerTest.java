package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    Server s = new Server();

    @BeforeEach
    void setUp() throws IOException {
        s.start();
    }

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        s.stop();
    }

    @Test
    void setupTest() throws IOException {
        Socket ss = new Socket("localhost", 8080);
        InputStream in = ss.getInputStream();
        OutputStream out = ss.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String msg = "Hello from test!";
        writer.println(msg);
        String response = reader.readLine();
        ss.close();
        assertTrue(response.equals(msg));
    }
    @Test
    void routeTest() throws Exception{

        Socket ss = new Socket("localhost", 8080);
        InputStream in = ss.getInputStream();
        OutputStream out = ss.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String msg = "GET /example HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Connection: keep-alive\r\n" +
                "Cookie: sessionId=1234abcd; userId=5678efgh\r\n";

        writer.println(msg);
        Stream<String> response = reader.lines();
        ss.close();
        assertTrue(response.equals(msg));


    }
}