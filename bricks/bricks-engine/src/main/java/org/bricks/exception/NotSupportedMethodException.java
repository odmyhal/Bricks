package org.bricks.exception;

public class NotSupportedMethodException extends RuntimeException {

	public NotSupportedMethodException(){
		super();
	}
	
	public NotSupportedMethodException(String message){
		super(message);
	}
}
