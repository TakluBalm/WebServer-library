package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

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
        @Controller(URL = "/prakhar")
        class BabuMoshai {
            @MethodHandler(method = "GET")
            void tp(){
                String time = "pass";
                return;
            }
        }
        @Controller(URL = "/tanuj")
        class JugalPrakharTanuj {
        }

        Socket ss = new Socket("localhost", 8080);
        InputStream in = ss.getInputStream();
        OutputStream out = ss.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String msg = "This is a routing message";
        writer.println(msg);
        String response = reader.readLine();
        ss.close();
        assertTrue(response.equals(msg));
    }
}