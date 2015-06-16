package org.bricks.core.entity;

public class Tuple<A, B> {
	
	private A first;
	private B second;
	
	public Tuple(){}

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
	
	public void setFirt(A a){
		first = a;
	}
	
	public void setSecond(B b){
		second = b;
	}
	
	public boolean equals(Object object){
		if(this.getClass() != object.getClass()){
			return false;
		}
		Tuple t = (Tuple) object;
		if(first == null){
			if(second == null){
				return false;
			}
			return second.equals(t.second);
		}
		if(second == null){
			if(first == null){
				return false;
			}
			return first.equals(t.first);
		}
		return first.equals(t.first) && second.equals(t.second);
	}
}
