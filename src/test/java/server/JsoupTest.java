package server;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

public class JsoupTest {
    public static void main(String[] args)
    {
        String html = "<html><body>Bruh Moment<div id=\"container\"><h1>Title</h1><p>Paragraph</p></div> ahjbsdhjbahjbd </body></html>";

        Document j = Jsoup.parse(html);
        Element x = j.selectFirst("body");
        
        List<TextNode> nodes = x.textNodes();
        System.out.println(nodes);
        for(TextNode node : nodes){
            node.text("Gruh");
        }
        System.out.println(x.text());

        // System.out.println(x.ownText());
    }
}
