package org.bricks.engine.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.core.entity.Point;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.view.SubjectView;

public class OverlapEvent extends BaseEvent<Entity>{
	
	private static final AtomicInteger crashNumberGenerator = new AtomicInteger(0);
	private SubjectView source;
	private SubjectView target;
	private Point touchPoint;
	private int crashNum;
	
	private static int generateCrashNumber(){
		return crashNumberGenerator.incrementAndGet();
	}
	
	public OverlapEvent(SubjectView target, SubjectView source, Point touchPoint, int crNumb){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = crNumb;
	}
	
	public OverlapEvent(SubjectView target, SubjectView source, Point touchPoint){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = generateCrashNumber();
	}
	
	public String sourceType(){
		return source.getSubject().getEntity().sourceType();
	}
	
	public Point getTouchPoint(){
		return touchPoint;
	}
	
	public SubjectView getSourceView(){
		return source;
	}
	
	public SubjectView getTargetView(){
		return target;
	}

	public Entity getEventSource() {
		return source.getSubject().getEntity();
	}

	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}
	
	public int getCrashNumber(){
		return crashNum;
	}
	

}
