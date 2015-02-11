package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

/**
 * Only checks events which already was added to target by addEvent method
 * It is redundant if target has at least one other checker
 * @author oleh
 *
 */
public class OwnEventChecker<T extends Liver> extends EventChecker<T>{

	private static final OwnEventChecker instance = new OwnEventChecker();
	
	private OwnEventChecker(){
		super(CheckerType.registerCheckerType());
		this.supplant(this.checkerType());
	}
	
	public boolean isActive(){
		return true;
	}
	
	public void activate(T target, long curTime){};
	
	@Override
	protected Event popEvent(T target, long eventTime) {
		return null;
	}
	
	public static final EventChecker instance(){
		return instance;
	}

}
