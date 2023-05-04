package server.exceptions;

public class HttpUnsupportedVersionException extends HttpException {
	public HttpUnsupportedVersionException(String message){
		super(message);
	}
}
