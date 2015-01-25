package org.bricks.engine.tool;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.Liver;

public class Live {
	
	private final Vector<Event> events = new Vector<Event>();
	private final Set<EventChecker> checkers = new HashSet<EventChecker>();
	private final Set<EventChecker> tmpAddCheckers = new HashSet<EventChecker>();
	private final Set<EventChecker> tmpDelCheckers = new HashSet<EventChecker>();
	private final Map<Integer, Event> eventHistory = new HashMap<Integer, Event>();

	public synchronized void registerEventChecker(EventChecker<? extends Liver> checker) {
		for(CheckerType cht : checker.supplants()){
			for(EventChecker ch : checkers){
				if(cht.equals(ch.checkerType())){
					tmpDelCheckers.add(ch);
				}
			}
		}
		tmpAddCheckers.add(checker);
	}
	
	public synchronized boolean unregisterEventChecker(EventChecker<? extends Liver> checker){
		return tmpDelCheckers.add(checker);
	}

	public boolean addEvent(Event e) {
		return events.add(e);
	}

	public synchronized boolean checkerRegistered(EventChecker<? extends Liver> checker) {
		return checkers.contains(checker);
	}
	
	public synchronized void refreshCheckers(){
		if(!tmpDelCheckers.isEmpty()){
			checkers.removeAll(tmpDelCheckers);
			tmpDelCheckers.clear();
		}
		if(!tmpAddCheckers.isEmpty()){
			for(EventChecker checker : tmpAddCheckers){
				checker.activate();
			}
			checkers.addAll(tmpAddCheckers);
			tmpAddCheckers.clear();
		}
	}

	public synchronized boolean hasChekers() {
		return !checkers.isEmpty();
	}

	public synchronized Collection<EventChecker> getCheckers() {
		return checkers;
	}
	
	public Event popEvent() {
		if(events.isEmpty()){
			return null;
		}
		return events.remove(0);
	}
	
	public Event putHistory(Event event){
		synchronized(eventHistory){
			return eventHistory.put(event.getEventGroupCode(), event);
		}
	}
	
	public Event getHistory(Event event){
		synchronized(eventHistory){
			return eventHistory.get(event.getEventGroupCode());
		}
	}
	
	public Event getHistory(int eventGroupCode){
		synchronized(eventHistory){
			return eventHistory.get(eventGroupCode);
		}
	}
	
	public Event removeHistory(int groupCode){
		synchronized(eventHistory){
			return eventHistory.remove(groupCode);
		}
	}
}
