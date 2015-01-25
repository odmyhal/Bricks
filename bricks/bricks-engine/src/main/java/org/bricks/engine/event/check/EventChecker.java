package org.bricks.engine.event.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.staff.Liver;

public abstract class EventChecker<T extends Liver> {
	
	private CheckerType type;
	private boolean active = true;
	/**
	 * When current checker added to entity, all checkers with types from this supplant list should be removed from entity
	 * @author Oleh Myhal
	 */
	private final Set<CheckerType> supplants = new HashSet<CheckerType>();
	
	public EventChecker(){
		this(CheckerType.registerCheckerType());
	}
	
	public EventChecker(CheckerType type){
		this.type = type;
	}
	
	protected abstract Event popEvent(T target, long eventTime);
	
	public boolean isActive(){
		return active;
	}
	
	public void disable(){
		this.active = false;
	}
	
	public void activate(){
		this.active = true;
	}
/*	public void setActive(boolean active){
		this.active = active;
	}
	*/
	private Event fetchEvent(T target, long eventTime){
		Event res = target.popEvent();
		if(res == null){
			res = popEvent(target, eventTime);
		}
		return res;
	}
	
	public final void checkEvents(T target, long currentTime){
		while(target.alive()){
			//Synchronize with OverlapChecker.popEvent()
			synchronized(target){
				Event e = fetchEvent(target, currentTime);
				if(e == null){
					break;
				}
				e.setEventTime(currentTime);
				EventHandlerManager.processEvent(target, e);
			}
		}
	}
	
	public CheckerType checkerType(){
		return type;
	}
	
	public void supplant(CheckerType chType){
		supplants.add(chType);
	}
	
	public Collection<CheckerType> supplants(){
		return supplants;
	}
}
