package server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;

import javassist.compiler.SyntaxError;
import server.exceptions.InvalidSyntaxException;

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

    public HTMLResponse setBodyFromString(String document) {
        byte[] body = document.getBytes();
        super.setBody(body);

        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(htmlPath));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return builder.toString();
    }

    // private static void traverseLeafNodes(Element element, HashMap<String,
    // Resource> mp) throws Exception {
    // List<Node> childNodes = element.childNodes();

    // for (Node childNode : childNodes) {
    // if (childNode instanceof TextNode) {
    // TextNode textNode = (TextNode) childNode;
    // String text = textNode.text();
    // for(String key : mp.keySet()){
    // if(text.contains("{{" + key + "}}")){
    // text = text.replace("{{" + key + "}}", mp.get(key).getData());
    // }
    // }
    // textNode.text(text);

    // } else if (childNode instanceof Element) {
    // Element childElement = (Element) childNode;
    // traverseLeafNodes(childElement, mp);
    // }
    // }
    // }

    public HTMLResponse embedData(HashMap<String, Resource> mp) throws Exception {

        Document parsedHTML = Jsoup.parse(this.toString());
        Elements elements = parsedHTML.getAllElements();

        for (Element element : elements) {
            for (Attribute attribute : element.attributes()) {
                if (attribute.getKey().equals("__attrs__")) {
                    String value = attribute.getValue();
                    // src = %source; alt = %text
                    String[] attributesToChange = value.split(";");
                    for (String entry : attributesToChange) {
                        String[] attrVal = entry.split("=");
                        if (attrVal.length != 2) {
                            throw new InvalidSyntaxException("Invalid syntax on element " + element.tagName());
                        }

                        String val = attrVal[1].strip();

                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < val.length(); i++) {
                            if (val.charAt(i) == '%') {
                                int start = ++i;
                                if (i >= val.length()) {
                                    throw new InvalidSyntaxException("Invalid syntax");
                                }
                                if (checkAlpha(val.charAt(i))) {
                                    while (i < val.length() && isValid(val.charAt(i))) {
                                        i++;
                                    }
                                    if (mp.get(val.substring(start, i)) == null) {
                                        throw new InvalidSyntaxException("Variable value not provided");
                                    }
                                    builder.append(mp.get(val.substring(start, i)).getData());
                                    i--;
                                } else if (val.charAt(i) == '%') {
                                    builder.append('%');
                                }
                            } else {
                                builder.append(val.charAt(i));
                            }
                        }

                        element.attr(attrVal[0].strip(), builder.toString());
                        element.removeAttr("__attrs__");
                    }

                }
            }

            List<TextNode> nodes = element.textNodes();
            for (TextNode node : nodes) {
                String nodeText = node.text().strip();
                if (nodeText.startsWith("{{") && nodeText.endsWith("}}")) {
                    String actualText = nodeText.substring(2, nodeText.length() - 2);
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < actualText.length(); i++) {
                        if (actualText.charAt(i) == '%') {
                            int start = ++i;
                            if (i >= actualText.length()) {
                                throw new InvalidSyntaxException("Invalid syntax");
                            }
                            if (checkAlpha(actualText.charAt(i))) {
                                while (i < actualText.length() && isValid(actualText.charAt(i))) {
                                    i++;
                                }
                                if (mp.get(actualText.substring(start, i)) == null) {
                                    throw new InvalidSyntaxException("Variable value not provided");
                                }
                                builder.append(mp.get(actualText.substring(start, i)).getData());
                                i--;
                            } else if (actualText.charAt(i) == '%') {
                                builder.append('%');
                            }
                        } else {
                            builder.append(actualText.charAt(i));
                        }
                    }
                    node.text(builder.toString());
                }
            }
        }
        // traverseLeafNodes(parsedHTML.selectFirst("html"), mp);

        System.out.println(parsedHTML.html());
        setBodyFromString(parsedHTML.html());
        return this;
    }

    private boolean checkAlpha(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
    }

    private boolean isValid(char c) {

        return checkAlpha(c) || c == '_' || (c <= '9' && c >= '0');
    }

    // public String fillTags(String line,HashMap<String,Resource> mp) throws
    // Exception {
    // for(String x : mp.keySet()){
    // StringBuilder s = new StringBuilder();
    // s.append("{{");
    // s.append(x);
    // s.append("}}");
    // String target = s.toString();
    //
    //
    // int i = line.indexOf(target);
    // if(i!=-1){
    // String type = mp.get(x).getType();
    // switch(type){
    // case "image":
    // try{
    // String checkImg=line.substring(i-9,i-2);
    // if(checkImg.equals("img src"))
    // {
    // line = line.replace(target, mp.get(x).getData());
    // }
    // else
    // {
    //
    // }
    //
    // }
    // catch (Exception e)
    // {
    // System.out.println("");
    // }
    // }
    // }
    //
    //// line = line.replace(target, mp.get(x).getData());
    // }
    //
    // return line;
    //
    // }
}
