package org.bricks.engine.event.handler;

import org.bricks.engine.event.PrintOverlapEvent;
import com.odmyha.weapon.Cannon;

public class CannonOverlapEventHandler implements EventHandler<Cannon, PrintOverlapEvent> {
	public void processEvent(Cannon target, PrintOverlapEvent event) {
		target.hitCannon(event);
	}
}