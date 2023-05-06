package server;

import org.junit.jupiter.api.Test;

import server.HTMLResponse;
import server.Resource;
import server.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HTMLResponseTest {
//    @Test
    public void startServer() throws Exception{
        Server server = new Server();
        server.start();
        while(true){}
    }

    @Test
    public void testHTMLbody() throws IOException {
        HTMLResponse r = new HTMLResponse("1.1", "src/test/resources/test_1.html");
        try {
            FileOutputStream byteout = new FileOutputStream("src/test/resources/test_1_response.html");
            byte[] body = r.getBody();
            byteout.write(body);
            byteout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] f1 = Files.readAllBytes(Paths.get("src/test/resources/test_1.html"));
        byte[] f2 = Files.readAllBytes(Paths.get("src/test/resources/test_1_response.html"));
        assertEquals(f1.length, f2.length);
        for(int i = 0; i < f1.length; i++){
            assertEquals(f1[i], f2[i]);
        }
    }

    @Test
    public void testAttributeEmbedding() throws Exception {
        HTMLResponse r = new HTMLResponse("1.1", "src/test/resources/test_1_response.html");
        HashMap<String, Resource> mp = new HashMap<>();
		List<String> bruh = new ArrayList<>();
		bruh.add("Jugal");
		bruh.add("Prakhar");
		bruh.add("Tanuj");
		List<String> lmao = new ArrayList<>();
		lmao.add("Gautam");
		lmao.add("Vinay");
		lmao.add("Pajji");
		mp.put("bruh", new Resource("iterable").loadData(bruh));
		mp.put("name", new Resource("text").loadData("JPT"));
		mp.put("lmao", new Resource("iterable").loadData(lmao));
        r.embedData(mp);
    }

    @Test
    public void testImageEmbedding() throws Exception{
        HTMLResponse r = new HTMLResponse("1.1","src/test/resources/testImgEmb.html");
        HashMap<String,Resource> mp=new HashMap<>();
        mp.put("This is img",new Resource("image").loadData("src/test/resources/test-img.jpeg"));
        r.embedData(mp);
    }

}