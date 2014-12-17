package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

public abstract class SingleEventChecker<T extends Liver> extends EventChecker<T>{
	
	private boolean alive = true;
	
	public SingleEventChecker(){
		super();
	}
	
	public SingleEventChecker(CheckerType type){
		super(type);
	}

	@Override
	protected Event popEvent(T target) {
		if(alive && ready(target)){
			alive = false;
			target.unregisterEventChecker(this);
			return produceEvent(target);
		}
		return null;
	}
	
	protected abstract boolean ready(T target);
	
	protected abstract Event produceEvent(T target);

}
