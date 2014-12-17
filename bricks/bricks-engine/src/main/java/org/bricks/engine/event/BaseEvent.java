package org.bricks.engine.event;

public abstract class BaseEvent<L extends EventSource> implements Event {
	
	public abstract L getEventSource();
	public static final int touchEventCode = 1;
	private long eventTime;
	
	public long getEventTime(){
		return eventTime;
	}
	public void setEventTime(long l){
		this.eventTime = l;
	}
	
}
