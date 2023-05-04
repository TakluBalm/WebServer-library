package server;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {
    public static void main(String[] args)
    {
        String html = "<html><body><div id=\"container\"><h1>Title</h1><p>Paragraph</p></div></body></html>";

        Document j = Jsoup.parse(html);
        for(Element e : j.getAllElements()){
            System.out.println("*********************");
            System.out.println(e);
            System.out.println("*********************");
        }
    }
}
