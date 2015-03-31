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
	
	public boolean equals(Object object){
		if(object instanceof Tuple){
			Tuple t = (Tuple) object;
			return first.equals(t.first) && second.equals(t.second);
		}
		return false;
	}
}
