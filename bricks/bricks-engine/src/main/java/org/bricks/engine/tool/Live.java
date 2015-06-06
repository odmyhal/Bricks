package org.bricks.engine.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.Liver;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;

public class Live implements Iterable<EventChecker>{
	
	private final Quarantine<Event> events = new Quarantine<Event>(15);
	
	private final Loop<EventChecker> checkers = new LinkLoop<EventChecker>();
	private final Quarantine<EventChecker> tmpAddCheckers = new Quarantine<EventChecker>(5);
	private final Quarantine<EventChecker> tmpDelCheckers = new Quarantine<EventChecker>(5);
	private final Map<Integer, Event> eventHistory = new HashMap<Integer, Event>();
	
	private final Liver liver;
	
	public Live(Liver liver){
		this.liver = liver;
	}

	public void registerEventChecker(EventChecker<? extends Liver> checker) {
		tmpAddCheckers.push(checker);
	}
	
	public void unregisterEventChecker(EventChecker<? extends Liver> checker){
		tmpDelCheckers.push(checker);
	}

	public void addEvent(Event e) {
		events.push(e);
	}
	
	public void refreshCheckers(long currentTime){
		for(EventChecker<?> checker : tmpDelCheckers){
			checkers.remove(checker);
		}
		for(EventChecker<Liver> checker : tmpAddCheckers){
			for(CheckerType cht : checker.supplants()){
				Iterator<EventChecker> iterator = checkers.iterator();
				while(iterator.hasNext()){
					if(cht.equals(iterator.next().checkerType())){
						iterator.remove();
					}
				}
			}
			checker.activate(liver, currentTime);
			checkers.add(checker);
		}
	}

	public boolean hasChekers() {
		return !checkers.isEmpty();
	}

	public Event popEvent() {
		return events.poll();
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

	public Iterator<EventChecker> iterator() {
		return checkers.iterator();
	}
}
