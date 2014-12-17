package org.bricks.engine.event;

public interface Event {
	
	EventSource getEventSource();
	int getEventGroupCode();
	public String sourceType();
	public long getEventTime();
	public void setEventTime(long l);
	
}
