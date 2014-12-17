package org.bricks.engine.event.handler;

import org.bricks.engine.event.OverlapEvent;
import com.odmyha.shoot.Ball;

public class BallOverlapEventHandler_2 implements EventHandler<Ball, OverlapEvent> {
	public void processEvent(Ball target, OverlapEvent event) {
		target.cannonHit(event);
	}
}