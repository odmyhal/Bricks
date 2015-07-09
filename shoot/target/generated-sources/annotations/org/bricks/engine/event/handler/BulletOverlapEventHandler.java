package org.bricks.engine.event.handler;

import org.bricks.engine.event.PrintOverlapEvent;
import com.odmyha.weapon.Bullet;

public class BulletOverlapEventHandler implements EventHandler<Bullet, PrintOverlapEvent> {
	public void processEvent(Bullet target, PrintOverlapEvent event) {
		target.faceShield(event);
	}
}