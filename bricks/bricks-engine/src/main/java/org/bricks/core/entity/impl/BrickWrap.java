package org.bricks.core.entity.impl;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.Printable;

public abstract class BrickWrap<I extends PointSetPrint> implements Printable<I>{
	
	protected Brick brick;
	protected PrintStore<? extends BrickWrap, I> printStore;
	
	public BrickWrap(Brick brick){
//		brick.initialize();
		this.brick = brick;
		printStore = new PrintStore(this);
//		printStore.initPrint();
	}
/*	
	public BrickWrap(Brick brick){
		brick.initialize(origin);
		this.brick = brick;
	}
*/
	
	public Point getCenter() {
		return brick.getCenter();
	}
	
	public Brick getBrick(){
		return brick;
	}
	
	public float getWeight(){
		return brick.getWeight();
	}
	
	public void translate(int x, int y){
		brick.translate(x, y);
	}
	
	public void rotate(float rad, Point central){
		brick.rotate(rad, central);
	}
	
	public void adjustCurrentPrint(){
		printStore.adjustCurrentPrint();
	}
	
	public I getInnerPrint(){
		return printStore.getInnerPrint();
	}
	
	public I getSafePrint(){
		return printStore.getSafePrint();
	}
	
	public I print(){
		return (I) new PointSetPrint(printStore);
	}
}
