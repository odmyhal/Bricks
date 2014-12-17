package org.bricks.core.entity;

public class Tuple<A, B> {
	
	private A first;
	private B second;

	public Tuple(A a, B b){
		first = a;
		second = b;
	}
	
	public A getFirst(){
		return first;
	}
	
	public B getSecond(){
		return second;
	}
}
