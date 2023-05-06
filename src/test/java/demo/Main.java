package demo;

import java.io.IOException;

import ORM.Manager.Configuration;
import ORM.Manager.SessionFactory;
import server.Properties;
import server.Server;

public class Main {
	public static SessionFactory sf;
	public static void main(String args[]){
		Properties p = new Properties();
		p.setPoolSize(4).setTimeout(5000);

		Configuration config = new Configuration("src/test/java/demo/config.txt", false);
		try {
			sf = config.getFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Server server = new Server(p);
		try {
			server.start();
			System.out.println("The Server will run on seperate thread.\nUser is free to handle the server from this thread");
			System.out.println("Press Ctrl-C to shutdown the server");
			while(true){}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
