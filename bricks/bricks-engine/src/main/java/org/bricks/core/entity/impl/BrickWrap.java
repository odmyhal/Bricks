package org.bricks.core.entity.impl;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;

public abstract class BrickWrap {
	
	private Brick brick;
	
	public BrickWrap(Brick brick){
//		brick.initialize();
		this.brick = brick;
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
}
