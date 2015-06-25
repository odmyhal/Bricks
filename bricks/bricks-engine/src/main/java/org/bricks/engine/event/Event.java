package org.bricks.engine.event;

import org.bricks.engine.staff.AvareTimer;

public interface Event extends AvareTimer{
	
	EventSource getEventSource();
	int getEventGroupCode();
	public String sourceType();
	public long getEventTime();
//	public void timerSet(long l);
	
}
