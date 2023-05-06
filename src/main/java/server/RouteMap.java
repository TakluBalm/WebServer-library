package server;

import java.util.*;

public class RouteMap implements Map<Route, Invocation>{

	static class Node{
		Map<String, Node> next;
		Invocation value;

		Node(){
			next = new HashMap<>();
			value = null;
		}
	}

	private class RouteMapEntry implements Entry<Route, Invocation>{

		Route key;
		Invocation value;

		@Override
		public Route getKey() {
			return key;
		}

		@Override
		public Invocation getValue() {
			return value;
		}

		@Override
		public Invocation setValue(Invocation invocation) {
			value = invocation;
			return value;
		}

		public RouteMapEntry(Route key, Invocation value){
			this.key = key;
			this.value = value;
		}
	}
	private Node root = new Node();
	private int size = 0;

	@Override
	public void clear() {
		root = new Node();
		size = 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		return containsValue(root, value);
	}

	private boolean containsValue(Node root, Object value){
		if(root.value != null && root.value.equals(value)) return true;

		for(Node next: root.next.values()){
			if(next != null && containsValue(next, value)){
				return true;
			}
		}

		return false;
	}

	@Override
	public Set<Entry<Route, Invocation>> entrySet() {
		return entrySet(root);
	}

	private Set<Entry<Route, Invocation>> entrySet(Node node){
		Set<Entry<Route, Invocation>> r = new HashSet<>();
		Set<Entry<String, Node>> head = node.next.entrySet();
		for(Entry<String, Node> entry: head) {
			Node en = entry.getValue();
			String ek = entry.getKey();
			if (en.value != null) {
				r.add(new RouteMapEntry(new Route(ek, ""), en.value));
			}
			Set<Entry<Route, Invocation>> sub =  entrySet(en);
			for(Entry<Route, Invocation> e: sub){
				Route route = e.getKey();
				r.add(new RouteMapEntry(new Route(ek, route.Path.equals("")? route.Method : route.Method + "/" + route.Path), e.getValue()));
			}
		}
		return r;
	}

	@Override
	public Invocation get(Object key) {

		String[] steps = key.toString().split("/");

		Node cur = root;
		for(int i = 0; i < steps.length; i++){
			Node next = cur.next.get(steps[i]);
			if(next == null){
				next = cur.next.get("__var__");
				if(next == null) return null;
			}
			cur = next;
		}
		return cur.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<Route> keySet() {
		return keySet(root);
	}

	private Set<Route> keySet(Node root){
		Set<Entry<String, Node>> s = root.next.entrySet();
		Set<Route> r = new HashSet<>();
		for(Entry<String, Node> entry : s){
			Node n = entry.getValue();
			if(n.value != null){
				r.add(new Route(entry.getKey(), ""));
			}
			Set<Route> sub = keySet(n);
			for(Route route: sub){
				r.add(new Route(entry.getKey(), route.Path.equals("") ? route.Method: route.Method + "/" + route.Path));
			}
		}
		return r;
	}

	@Override
	public Invocation put(Route arg0, Invocation arg1) {
		String[] steps = arg0.toString().split("/");
		Node cur = root;

		for(int i = 0; i < steps.length; i++){
			if(steps[i].length() > 0 && steps[i].charAt(0) == '{' && steps[i].charAt(steps[i].length()-1) == '}'){
				steps[i] = "__var__";
			}
			Node next = cur.next.get(steps[i]);
			if(next == null){
				next = new Node();
				cur.next.put(steps[i], next);
			}
			cur = next;
		}
		cur.value = arg1;
		return cur.value;
	}

	@Override
	public void putAll(Map<? extends Route, ? extends Invocation> m) {
		for(Entry<? extends Route, ? extends Invocation> entry: m.entrySet()){
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Invocation remove(Object key) {
		String[] steps = key.toString().split("/");

		Node cur = root;
		for(int i = 0; i < steps.length; i++){
			Node next = cur.next.get(steps[i]);
			if(next == null){
				next = cur.next.get("__var__");
				if(next == null) return null;
			}
			cur = next;
		}
		Invocation copy = cur.value;
		cur.value = null;
		return copy;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<Invocation> values() {
		Collection<Invocation> c = values(root);
		if(root.value != null) c.add(root.value);
		return c;
	}

	public Collection<Invocation> values(Node root){
		Collection<Invocation> superCollection = new ArrayList<>();
		for(Node node: root.next.values()){
			Collection<Invocation> c = values(node);
			if(node.value != null) c.add(node.value);
			superCollection.addAll(c);
		}
		return superCollection;
	}

}
