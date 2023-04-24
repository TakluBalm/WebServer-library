package routeTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Controller;
import server.Server;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class routeTest {
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
    void routeTest() throws Exception{
        @Controller(URL = "/tanuj")
        class BabuMoshai {

        }
        @Controller(URL = "/prakhar")
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
