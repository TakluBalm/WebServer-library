package server;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageResponseTest {
    @Test
    public void testImageResponse() {
        ImageResponse ir = new ImageResponse("1.1", "testImage.png");
        assertEquals(ir.getHeaderValue("content-type"), "image/png");
    }

    @Test
    public void testContentHeader() {
        ImageResponse ir = new ImageResponse("1.1", "testImage.jpeg");
        ir.setHeader("Content-Type", "application/json").setHeader("tanuj-bhai", "katai-harami").setStatusCode(300);
        assertEquals(ir.getHeaderValue("content-type"), "image/jpeg");
        assertEquals(ir.getHeaderValue("tanuj-bhai"), "katai-harami");
        assertEquals(ir.getStatusCode(), 300);
    }

    @Test
    public void testImageBody() {
        ImageResponse ir = new ImageResponse("1.1", "D:\\Downloads\\test.jpg");
        ir.setBody("C:\\Users\\Acer\\Desktop\\WIN_20220322_16_28_37_Prosteg.jpg");

        // Assume that `responseString` is a string containing the HTTP response

        try {
            FileWriter fout = new FileWriter("C:\\Users\\Acer\\Desktop\\response.jpg");
            String s = new String(ir.getBody());
//            for(byte b: ir.getBody()){
//                data.append((char)b);
//            }
            fout.write(s);
            fout.close();
            System.out.println("Response saved to response.html");
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(ir.toString());
    }

    @Test
    public void checkFromBrowser() throws IOException {
        Server server = new Server();
        server.start();
        while (true){}
    }
}