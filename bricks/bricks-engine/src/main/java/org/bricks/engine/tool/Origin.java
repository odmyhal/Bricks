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
/*	
	public C source(){
		return source;
	}
	*/
}
