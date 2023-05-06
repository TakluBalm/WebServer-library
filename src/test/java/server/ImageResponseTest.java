package server;

import org.junit.jupiter.api.Test;

import server.ImageResponse;
import server.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ImageResponseTest {
    @Test
    public void testImageResponse() {
        ImageResponse ir = new ImageResponse("1.1", "src/test/resources/test-img.jpeg");
        assertEquals(ir.getHeaderValue("content-type"), "image/jpeg");
    }

    @Test
    public void testContentHeader() {
        ImageResponse ir = new ImageResponse("1.1", "src/test/resources/test-img.jpeg");
        ir.setHeader("tanuj-bhai", "katai-harami").setStatusCode(300);
        assertEquals(ir.getHeaderValue("content-type"), "image/jpeg");
        assertEquals(ir.getHeaderValue("tanuj-bhai"), "katai-harami");
        assertEquals(ir.getStatusCode(), 300);
    }

    @Test
    public void testImageBody() throws IOException {
        ImageResponse ir = new ImageResponse("1.1", "src/test/resources/test-img.jpeg");

        try {
            FileOutputStream byteout = new FileOutputStream("src/test/resources/response.jpeg");
			byte[] body = ir.getBody();
            byteout.write(body);
			byteout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/test-img.jpeg"));
		byte[] f2 = Files.readAllBytes(Paths.get("src/test/resources/response.jpeg"));
		assertEquals(f1.length, f2.length);
		for(int i = 0; i < f1.length; i++){
			assertEquals(f1[i], f2[i]);
		}
    }

//    @Test
    public void checkFromBrowser() throws IOException {
        Server server = new Server();
        server.start();
        while (true){}
    }
}