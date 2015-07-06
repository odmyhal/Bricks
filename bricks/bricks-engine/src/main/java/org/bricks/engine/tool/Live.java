package org.bricks.engine.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.AvareTimer;
import org.bricks.engine.staff.Liver;
import org.bricks.utils.Cache;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;
import org.bricks.utils.Quarantine;

public class Live implements Iterable<EventChecker>, AvareTimer{
	
	private final Quarantine<Event> events = new Quarantine<Event>(50);
	
	private final Loop<EventChecker> checkers = new LinkLoop<EventChecker>();
	private final Quarantine<EventChecker> tmpAddCheckers = new Quarantine<EventChecker>(10);
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
			if(applyChecker(checker)){
				checker.activate(liver, currentTime);
				checkers.add(checker);
			}
		}
	}
	
	private boolean applyChecker(EventChecker newChecker){
		for(EventChecker check : checkers){
			if(check.abort(newChecker)){
				return false;
			}
		}
		Iterator<EventChecker> iterator = checkers.iterator();
		while(iterator.hasNext()){
			EventChecker check = iterator.next();
			if(newChecker.supplant(check)){
				iterator.remove();
				continue;
			}
		}
		return true;
	}

	public boolean hasChekers() {
		return !checkers.isEmpty();
	}
	
	/**
	 * Call ONLY in motor thread
	 * @param time
	 */
	public void timerSet(long time){
		if(hasChekers()){
			for(EventChecker checker : checkers){
				if(checker instanceof AvareTimer){
					((AvareTimer) checker).timerSet(time);
				}
			}
		}
		if(hasEvents()){
			LinkLoop<Event> tmpLoop = Cache.get(LinkLoop.class);
			while(hasEvents()){
				Event e = popEvent();
				e.timerSet(time);
				tmpLoop.add(e);
			}
			for(Event e : tmpLoop){
				addEvent(e);
			}
			tmpLoop.clear();
			Cache.put(tmpLoop);
		}
	}
	
	/**
	 * Call ONLY in motor thread
	 * @param time
	 */
	public void timerAdd(long time){
		if(hasChekers()){
			for(EventChecker checker : checkers){
				if(checker instanceof AvareTimer){
					((AvareTimer) checker).timerAdd(time);
				}
			}
		}
		if(hasEvents()){
			LinkLoop<Event> tmpLoop = Cache.get(LinkLoop.class);
			while(hasEvents()){
				Event e = popEvent();
				e.timerAdd(time);
				tmpLoop.add(e);
			}
			for(Event e : tmpLoop){
				addEvent(e);
			}
			tmpLoop.clear();
			Cache.put(tmpLoop);
		}
	}

	public boolean hasEvents(){
		return !events.isEmpty();
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
