package org.bricks.engine.tool;

public abstract class Origin<C> {

	public C source;
	
	public Origin(C cntr){
		this.source = cntr;
	}
	
	public abstract void add(Origin<C> trn);
	
	public abstract void set(Origin<C> init);
	
	public abstract boolean isZero();
	
	public abstract void mult(float k);
	
	public abstract void sub(Origin<C> sub);
/*	
	public C source(){
		return source;
	}
	*/
	
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		return source.equals(((Origin) obj).source);
	}
}
