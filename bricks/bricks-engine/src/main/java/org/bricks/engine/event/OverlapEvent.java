package org.bricks.engine.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Subject;

public class OverlapEvent<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject>, P> 
	extends BaseEvent<Entity>{
	
	private static final AtomicInteger crashNumberGenerator = new AtomicInteger(0);
	private T target;
	protected K source;
	protected P touchPoint;
	private int crashNum;
	
	private static int generateCrashNumber(){
		return crashNumberGenerator.incrementAndGet();
	}
	
	public OverlapEvent(T target, K source, P touchPoint, int crNumb){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = crNumb;
	}
	
	public OverlapEvent(T target, K source, P touchPoint){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = generateCrashNumber();
	}
	
	public String sourceType(){
		return source.getTarget().getEntity().sourceType();
	}
	
	public P getTouchPoint(){
		return touchPoint;
	}
	
	public K getSourcePrint(){
		return source;
	}
	
	public T getTargetPrint(){
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
