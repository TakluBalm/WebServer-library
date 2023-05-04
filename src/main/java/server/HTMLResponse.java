package server;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;

import javax.swing.text.html.HTML;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;

public class HTMLResponse extends Response {
    String htmlPath;

    public HTMLResponse(String version, String htmlPath) {
        super(version);
        this.htmlPath = htmlPath;
        this.setHeader("Content-Type", "text/html");
        this.setBody(this.htmlPath);
    }

    public HTMLResponse setBody(String htmlPath) {
        byte[] body = null;
        try {
            FileInputStream fis = new FileInputStream(htmlPath);
            body = fis.readAllBytes();
        } catch (Exception e) {
            System.out.println(7);
        }

        super.setBody(body);
        return this;
    }

    public HTMLResponse setBodyFromString(String document){
        byte[] body = document.getBytes();
        super.setBody(body);

        return this;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(htmlPath));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (Exception e){
            System.out.println(e);
        }

        return builder.toString();
    }

    private static void traverseLeafNodes(Element element, HashMap<String, Resource> mp) throws Exception {
        List<Node> childNodes = element.childNodes();

        for (Node childNode : childNodes) {
            if (childNode instanceof TextNode) {
                TextNode textNode = (TextNode) childNode;
                String text = textNode.text();
                for(String key : mp.keySet()){
                    if(text.contains("{{" + key + "}}")){
                        System.out.println("found ya bich");
                        text = text.replace("{{" + key + "}}", mp.get(key).getData());
                    }
                }
                textNode.text(text);

            } else if (childNode instanceof Element) {
                Element childElement = (Element) childNode;
                traverseLeafNodes(childElement, mp);
            }
        }
    }

    public HTMLResponse embedData(HashMap<String, Resource> mp) throws Exception {

        Document parsedHTML = Jsoup.parse(this.toString());
        Elements elements = parsedHTML.getAllElements();

        for(Element element : elements){
            for(Attribute attribute : element.attributes()){
                String value = attribute.getValue();
                for(String key : mp.keySet()){
                    if(value.contains("{{" + key + "}}")){
                        value = value.replace("{{" + key + "}}", mp.get(key).getData());
                    }
                }
                attribute.setValue(value);
            }
        }
        traverseLeafNodes(parsedHTML.selectFirst("html"), mp);

        System.out.println(parsedHTML.html());
        setBodyFromString(parsedHTML.html());
        return this;
    }

//    public String fillTags(String line,HashMap<String,Resource> mp) throws Exception {
//        for(String x : mp.keySet()){
//            StringBuilder s = new StringBuilder();
//            s.append("{{");
//            s.append(x);
//            s.append("}}");
//            String target = s.toString();
//
//
//            int i = line.indexOf(target);
//            if(i!=-1){
//                String type = mp.get(x).getType();
//                switch(type){
//                    case "image":
//                        try{
//                            String checkImg=line.substring(i-9,i-2);
//                            if(checkImg.equals("img src"))
//                            {
//                                line = line.replace(target, mp.get(x).getData());
//                            }
//                            else
//                            {
//
//                            }
//
//                        }
//                        catch (Exception e)
//                        {
//                            System.out.println("");
//                        }
//                }
//            }
//
////            line = line.replace(target, mp.get(x).getData());
//        }
//
//        return line;
//
//    }
}
