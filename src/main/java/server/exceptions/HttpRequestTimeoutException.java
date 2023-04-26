package server.exceptions;

public class HttpRequestTimeoutException extends HttpException{
	public HttpRequestTimeoutException(String message){
		super(message);
	}
}
