package org.bricks.engine.event.handler;

import org.bricks.engine.event.OverlapEvent;
import com.odmyha.weapon.Bullet;

public class BulletOverlapEventHandler implements EventHandler<Bullet, OverlapEvent> {
	public void processEvent(Bullet target, OverlapEvent event) {
		target.faceShield(event);
	}
}