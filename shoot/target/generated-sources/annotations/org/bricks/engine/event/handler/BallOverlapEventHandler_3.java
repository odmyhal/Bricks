package org.bricks.engine.event.handler;

import org.bricks.engine.event.PrintOverlapEvent;
import com.odmyha.shoot.Ball;

public class BallOverlapEventHandler_3 implements EventHandler<Ball, PrintOverlapEvent> {
	public void processEvent(Ball target, PrintOverlapEvent event) {
		target.faceBullet(event);
	}
}