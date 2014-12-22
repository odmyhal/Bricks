package org.bricks.exception;

import java.util.Collection;

public class Validate {

	public static void isTrue(boolean expr){
		isTrue(expr, "Wrong assumption");
	}
	
	public static void isTrue(boolean expr, String msg){
		if(!expr){
			throw new ValidateException(msg);
		}
	}
	
	public static void isFalse(boolean expr){
		isFalse(expr, "Wrong assumption that expression is false");
	}
	
	public static void isFalse(boolean expr, String msg){
		if(expr){
			throw new ValidateException(msg);
		}
	}
	
	public static void notEmpty(Collection data, String msg){
		if(data.isEmpty()){
			throw new ValidateException(msg);
		}
	}
	
}
