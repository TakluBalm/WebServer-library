package server;

public class Properties {
	protected int poolSize = Runtime.getRuntime().availableProcessors();
	protected int inactivityTimeout = 2000;

	Properties setPoolSize(int poolsize){
		poolSize = poolsize;
		return this;
	}

	Properties setTimeout(int timeout){
		this.inactivityTimeout = timeout;
		return this;
	}
}
