package org.bricks.engine.event.handler;

import org.bricks.engine.event.PrintOverlapEvent;
import com.odmyha.weapon.Cannon;

public class CannonOverlapEventHandler_1 implements EventHandler<Cannon, PrintOverlapEvent> {
	public void processEvent(Cannon target, PrintOverlapEvent event) {
		target.doNothing(event);
	}
}