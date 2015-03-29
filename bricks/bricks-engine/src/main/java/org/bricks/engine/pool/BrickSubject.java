package org.bricks.engine.pool;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.PrintableBrickWrap;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;

public class BrickSubject<E extends Entity, I extends SubjectPrint> extends BaseSubject<E, I, Fpoint, Roll>  implements PrintableBrickWrap<I>{

	protected Brick brick;
//	protected PrintStore<? extends BrickWrap, I> printStore;
	
	public BrickSubject(Brick brick){
		this.brick = brick;
//		initPrintStore();
//		printStore = new PrintStore(this);
	}
	
	public Point getCenter() {
		return brick.getCenter();
	}
	
	public Brick getBrick(){
		return brick;
	}
	
	public float getWeight(){
		return brick.getWeight();
	}
	
	public void translate(Origin<Fpoint> origin){
		this.translate(origin.source);
	}
	
	public void translate(Point p){
		this.translate(p.getX(), p.getY());
	}
	
	public void translate(int x, int y){
		brick.translate(x, y);
	}
	
	public void rotate(Roll roll, Origin<Fpoint> central){
		this.rotate(roll.getRotation(), central.source);
	}
	
	public void rotate(float rad, Origin<Point> origin){
		this.rotate(rad, origin.source);
	}
	
	public void rotate(float rad, Point central){
		brick.rotate(rad, central);
	}
	

	public I print(){
		return (I) new SubjectPrint(printStore);
	}
}
