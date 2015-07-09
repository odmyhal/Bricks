package org.bricks.engine.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.engine.staff.EntityCore;

public abstract class OverlapEvent<T, S, P, E extends EntityCore> extends BaseEvent<E>{

	private static final AtomicInteger crashNumberGenerator = new AtomicInteger(0);

	protected T target;
	protected S source;
	protected P touchPoint;
	private int crashNum;
	
	public OverlapEvent(T target, S source, P touchPoint){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = generateCrashNumber();
	}
	
	public T getTargetData(){
		return target;
	}
	
	public S getSourceData(){
		return source;
	}

	public P getTouchPoint(){
		return touchPoint;
	}
	
	private static int generateCrashNumber(){
		return crashNumberGenerator.incrementAndGet();
	}
	
	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}

	public int getCrashNumber(){
		return crashNum;
	}
}
