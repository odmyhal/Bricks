package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.staff.Liver;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;

public abstract class EventChecker<T extends Liver> {
	
	private CheckerType type;
	private boolean active = true;
	protected T entity;
	/**
	 * When current checker added to entity, all checkers with types from this supplant list should be removed from entity
	 * @author Oleh Myhal
	 */
	private final Loop<CheckerType> supplants = new LinkLoop<CheckerType>();
	private final Loop<CheckerType> aborts = new LinkLoop<CheckerType>();
/*	
	public EventChecker(){
		this(CheckerType.registerCheckerType());
	}
*/	
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
	
	public void activate(T target, long curTime){
		this.entity = target;
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
				e.timerSet(currentTime);
				EventHandlerManager.processEvent(target, e);
			}
		}
	}
	
	protected CheckerType checkerType(){
		return type;
	}
	
	public void addSupplant(CheckerType chType){
		supplants.add(chType);
	}
	
	public void addAbort(CheckerType chType){
		aborts.add(chType);
	}
	
	public boolean supplant(EventChecker<?> anotherChecker){
		return matchCheckerType(supplants, anotherChecker);
	}
	
	public boolean abort(EventChecker<?> anotherChecker){
		return matchCheckerType(aborts, anotherChecker);
	}
	
	private boolean matchCheckerType(Loop<CheckerType> types, EventChecker<?> checker){
		for(CheckerType chType : types){
			if(chType.match(checker.type)){
				return true;
			}
		}
		return false;
	}

}
