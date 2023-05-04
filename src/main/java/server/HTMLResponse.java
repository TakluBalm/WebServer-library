package server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import server.exceptions.InvalidSyntaxException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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

	private void ParseWithContext(Element root, Map<String, Resource> context) throws Exception{
		// System.out.println(root.tagName()+":"+root.childNodes());
		for (Attribute attribute : root.attributes()) {
			if (attribute.getKey().equals("__attrs__")) {
				String value = attribute.getValue();
				// src = /resource/%source; alt = %text
				String[] attributesToChange = value.split(";");
				for (String entry : attributesToChange) {
					String[] attrVal = entry.split("=");
					if (attrVal.length != 2) {
						throw new InvalidSyntaxException("Invalid syntax on element " + root.tagName());
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
								if (context.get(val.substring(start, i)) == null) {
									throw new InvalidSyntaxException("Variable value not provided");
								}
								builder.append(context.get(val.substring(start, i)).getData());
								i--;
							} else if (val.charAt(i) == '%') {
								builder.append('%');
							}
						} else {
							builder.append(val.charAt(i));
						}
					}

					root.attr(attrVal[0].strip(), builder.toString());
				}
				root.removeAttr("__attrs__");

			}
		}

		for (TextNode node : root.textNodes()) {
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
							if (context.get(actualText.substring(start, i)) == null) {
								throw new InvalidSyntaxException("Variable value not provided");
							}
							builder.append(context.get(actualText.substring(start, i)).getData());
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


	class Loop{
		Iterator<?> itr;
		String variable;
		int startIdx;

		public Loop(Iterable<?> loopIterable, String loopVar, int startIdx){
			itr = loopIterable.iterator();
			variable = loopVar;
			this.startIdx = startIdx;
		}

		public Object getNext(){
			return itr.next();
		}

		public String getVariable(){
			return variable;
		}

		public boolean hasNext(){
			return itr.hasNext();
		}

	}


	private Element embedDataHelper(Element root, Map<String, Resource> context) throws Exception{
		List<Node> nodes = root.childNodes();
		Stack<Loop> loops = new Stack<>();
		int skip = 0;

		Element element = new Element(root.tag(), root.baseUri(), root.attributes().clone());

		for(int i = 0; i < nodes.size(); i++){
			Node node = nodes.get(i);
			if(node instanceof Element && skip == 0){
				element.appendChild(embedDataHelper((Element)node, context));
			}else if(node instanceof TextNode){
				String text = ((TextNode)node).text().strip();
				if(text.startsWith("{%") && text.endsWith("%}")){
					String actualText = text.substring(2, text.length()-2).strip();
					String[] tokens = actualText.split(" ");
					
					if(tokens.length == 4 && tokens[0].equals("for") && tokens[2].equals("in")){
						Resource resource = context.get(tokens[3]);
						if(resource != null){
							Loop loop = new Loop((Iterable<?>)resource.getData(), tokens[1], i);
							loops.push(loop);
							if(loop.hasNext()){
								Object val = loop.getNext();
								Resource r = new Resource("text");
								r.loadData(val);
								context.put(loop.getVariable(), r);
							}else{
								skip++;
							}
						}else{
							throw new InvalidSyntaxException("Resouce not provided");
						}
					}else if(tokens.length == 2 && tokens[0].equals("end") && tokens[1].equals("for")){
						try{
							Loop loop = loops.pop();
							if(loop.hasNext()){
								loops.push(loop);
								Resource r = new Resource("text");
								r.loadData(loop.getNext());
								context.put(loop.getVariable(), r);
								i = loop.startIdx;
							}else{
								context.remove(loop.getVariable());
								if(skip > 0)	skip--;
							}
						}catch(EmptyStackException e){
							throw new InvalidSyntaxException("The Syntax of the HTML file is invalid");
						}
					}
				}else{
					TextNode copy = new TextNode(((TextNode) node).text());
					element.appendChild(copy);
				}
			}
		}

		ParseWithContext(element, context);
		// System.out.println(element.html());
		return element;
	}

    public HTMLResponse embedData(Map<String, Resource> mp) throws Exception {

        Document parsedHTML = Jsoup.parse(this.toString());
		Element parsedRoot = embedDataHelper(parsedHTML.root(), mp);

        System.out.println(parsedRoot.html());
        setBodyFromString(parsedRoot.html());
        return this;
    }

    private boolean checkAlpha(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
    }

    private boolean isValid(char c) {

        return checkAlpha(c) || c == '_' || (c <= '9' && c >= '0');
    }
}
