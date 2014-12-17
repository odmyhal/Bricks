package org.bricks.engine.event.handler;

import org.bricks.extent.event.FireEvent;
import com.odmyha.weapon.Cannon;

public class CannonFireEventHandler implements EventHandler<Cannon, FireEvent> {
	public void processEvent(Cannon target, FireEvent event) {
		target.shoot(event);
	}
}