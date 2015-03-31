package org.bricks.engine.event;

import org.bricks.core.entity.Point;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.Boundary;

public class BorderEvent extends BaseEvent<Boundary>{
	
	private Boundary  boundary;
	private Point touchPoint;
	private EntityPointsPrint target;
	
	public BorderEvent(EntityPointsPrint target, Boundary boundary, Point point){
		this.boundary = boundary;
		this.touchPoint = point;
		this.target = target;
	}

	public String sourceType() {
		return boundary.sourceType();
	}

	public Boundary getEventSource() {
		return boundary;
	}

	public Point getTouchPoint() {
		return touchPoint;
	}

	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}
	
	public EntityPointsPrint getTargetPrint(){
		return target;
	}
	
}
