package org.bricks.engine.event;

import org.bricks.core.entity.Point;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.view.SubjectView;

public class BorderEvent extends BaseEvent<Boundary>{
	
	private Boundary  boundary;
	private Point touchPoint;
	private SubjectView target;
	
	public BorderEvent(SubjectView target, Boundary boundary, Point point){
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
	
	public SubjectView getTargetView(){
		return target;
	}
	
}
