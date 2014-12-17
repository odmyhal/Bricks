package org.bricks.engine.event.handler;

import org.bricks.engine.event.BorderEvent;
import com.odmyha.shoot.Ball;

public class BallBorderEventHandler implements EventHandler<Ball, BorderEvent> {
	public void processEvent(Ball target, BorderEvent event) {
		target.borderSideTouch(event);
	}
}