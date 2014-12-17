package org.bricks.engine.event.handler;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.EventTarget;

public interface EventHandler<A extends EventTarget, B extends Event> {
	
	public void processEvent(A target, B event);

}
