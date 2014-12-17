package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

/**
 * Only checks events which already was added to target by addEvent method
 * It is redundant if target has at least one other checker
 * @author oleh
 *
 */
public class OwnEventChecker extends EventChecker{

	private static final OwnEventChecker instance = new OwnEventChecker();
	
	private OwnEventChecker(){}
	
	@Override
	protected Event popEvent(Liver target) {
		return null;
	}
	
	public static final EventChecker instance(){
		return instance;
	}

}
