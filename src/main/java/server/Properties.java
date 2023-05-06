package server;

public class Properties {
	protected int poolSize = Runtime.getRuntime().availableProcessors();
	protected int inactivityTimeout = 2000;
	protected int port = 8080;

	public Properties setPoolSize(int poolsize){
		poolSize = poolsize;
		return this;
	}

	public Properties setTimeout(int timeout){
		this.inactivityTimeout = timeout;
		return this;
	}

	public Properties setPort(int port){
		this.port = port;
		return this;
	}
}
