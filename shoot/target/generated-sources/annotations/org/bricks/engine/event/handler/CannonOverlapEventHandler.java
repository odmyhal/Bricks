package org.bricks.engine.event.handler;

import org.bricks.engine.event.OverlapEvent;
import com.odmyha.weapon.Cannon;

public class CannonOverlapEventHandler implements EventHandler<Cannon, OverlapEvent> {
	public void processEvent(Cannon target, OverlapEvent event) {
		target.hitCannon(event);
	}
}