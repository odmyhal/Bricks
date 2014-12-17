package org.bricks.engine.event.handler;

import org.bricks.engine.event.BorderEvent;
import com.odmyha.shoot.Ball;

public class BallBorderEventHandler_1 implements EventHandler<Ball, BorderEvent> {
	public void processEvent(Ball target, BorderEvent event) {
		target.borderCornerTouch(event);
	}
}