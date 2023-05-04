package server;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

public class JsoupTest {
    public static void main(String[] args)
    {
        String html =
		"<html>\n"+
			"<body>Bruh Moment\n"+
				"<div id=\"container\">\n"+
					"<h1>Title</h1>\n"+
					// "<p>Paragraph</p>\n"+
				"</div>\n"+
			"</body>\n"+
		"</html>\n";

        Document j = Jsoup.parse(html);
		Elements allElements = j.getAllElements();
		for(Element e:allElements){
			System.out.println(e.tagName());
		}
        Element x = j.selectFirst("div");
		x.append("<p>Newly added</p>");
		System.out.println("*********************************");
		for(Element e:allElements){
			System.out.println(e.tagName());
		}
        // List<TextNode> nodes = x.textNodes();
        // System.out.println(nodes);
        // for(TextNode node : nodes){
        //     node.text("Gruh");
        // }
        // System.out.println(x.text());

        // System.out.println(x.ownText());
    }
}
