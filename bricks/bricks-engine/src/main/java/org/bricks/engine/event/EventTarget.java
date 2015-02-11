package org.bricks.engine.event;

import java.util.Collection;

import org.bricks.engine.event.check.EventChecker;

public interface EventTarget {
	
	public void registerEventChecker(EventChecker checker);
	public boolean unregisterEventChecker(EventChecker checker);
	public boolean addEvent(Event e);
	public Event popEvent();
	public boolean checkerRegistered(EventChecker checker);
	public void refreshCheckers(long currentTime);
	public boolean hasChekers();
	public Collection<EventChecker> getCheckers();
	
	
	public void processEvents(long currentTime);
	
//	public void registerEventChecker(EventChecker checker);
//	public boolean unregisterEventChecker(EventChecker checker);
//	public boolean addEvent(Event e);
//	public boolean checkerRegistered(EventChecker checker);
//	public boolean hasChekers();
//	public Collection<EventChecker> getCheckers();

	public Event getHistory(int eventGroupCode);
	public Event putHistory(Event event);
	public Event removeHistory(int groupCode);
}
