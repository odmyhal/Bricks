package org.bricks.engine.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.core.entity.Point;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;

public class OverlapEvent extends BaseEvent<Entity>{
	
	private static final AtomicInteger crashNumberGenerator = new AtomicInteger(0);
	private SubjectPrint<? extends BrickSubject, ? extends EntityPrint> source;
	private SubjectPrint<? extends BrickSubject, ? extends EntityPrint> target;
	private Point touchPoint;
	private int crashNum;
	
	private static int generateCrashNumber(){
		return crashNumberGenerator.incrementAndGet();
	}
	
	public OverlapEvent(SubjectPrint target, SubjectPrint source, Point touchPoint, int crNumb){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = crNumb;
	}
	
	public OverlapEvent(SubjectPrint target, SubjectPrint source, Point touchPoint){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = generateCrashNumber();
	}
	
	public String sourceType(){
		return source.getTarget().getEntity().sourceType();
	}
	
	public Point getTouchPoint(){
		return touchPoint;
	}
	
	public SubjectPrint<? extends Subject, ? extends EntityPrint> getSourcePrint(){
		return source;
	}
	
	public SubjectPrint<? extends Subject, ? extends EntityPrint> getTargetPrint(){
		return target;
	}

	public Entity getEventSource() {
		return source.getTarget().getEntity();
	}

	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}
	
	public int getCrashNumber(){
		return crashNum;
	}
	

}
