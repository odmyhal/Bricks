package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

/**
 * 
 * @author oleh
 * @deprecated use ChunkEventChecker instead...
 * @param <T>
 */
@Deprecated
public abstract class DoubleEventChecker<T extends Liver> extends EventChecker<T>{
	
	private boolean needStart;
/*	
	public DoubleEventChecker(CheckerType chType){
		super();
		init();
	}
*/	
	public DoubleEventChecker(CheckerType type){
		super(type);
		init();
	}
	
	public void init(){
		needStart = true;
		activate(entity, System.currentTimeMillis());
	}

	@Override
	protected Event popEvent(T target, long eventTime) {
		if(needStart){
			needStart = false;
			return produceStartEvent(target);
		}else if(isActive() && ready(target)){
			disable();
			target.unregisterEventChecker(this);
			return produceFinishEvent(target);
		}
		return null;
	}
	
	protected abstract boolean ready(T target);
	
	protected abstract Event produceStartEvent(T target);
	
	protected abstract Event produceFinishEvent(T target);

}