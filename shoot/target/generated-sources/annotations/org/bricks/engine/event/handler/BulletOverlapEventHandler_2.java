package org.bricks.engine.event.handler;

import org.bricks.engine.event.OverlapEvent;
import com.odmyha.weapon.Bullet;

public class BulletOverlapEventHandler_2 implements EventHandler<Bullet, OverlapEvent> {
	public void processEvent(Bullet target, OverlapEvent event) {
		target.faceOtherCannon(event);
	}
}