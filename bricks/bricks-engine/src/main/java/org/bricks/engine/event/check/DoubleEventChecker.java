package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

public abstract class DoubleEventChecker<T extends Liver> extends EventChecker<T>{
	
	private boolean start, finish;
	
	public DoubleEventChecker(){
		super();
		init();
	}
	
	public DoubleEventChecker(CheckerType type){
		super(type);
		init();
	}
	
	public void init(){
		start = true;
		finish = true;
	}

	@Override
	protected Event popEvent(T target) {
		if(start){
			start = false;
			return produceStartEvent(target);
		}else if(finish && ready(target)){
			finish = false;
			target.unregisterEventChecker(this);
			return produceFinishEvent(target);
		}
		return null;
	}
	
	protected abstract boolean ready(T target);
	
	protected abstract Event produceStartEvent(T target);
	
	protected abstract Event produceFinishEvent(T target);

}