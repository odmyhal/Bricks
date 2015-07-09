package org.bricks.engine.event.handler;

import org.bricks.engine.event.PrintOverlapEvent;
import com.odmyha.weapon.Bullet;

public class BulletOverlapEventHandler_1 implements EventHandler<Bullet, PrintOverlapEvent> {
	public void processEvent(Bullet target, PrintOverlapEvent event) {
		target.faceBall(event);
	}
}