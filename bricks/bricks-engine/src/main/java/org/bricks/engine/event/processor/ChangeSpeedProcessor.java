package org.bricks.engine.event.processor;

import org.bricks.engine.event.control.SpeedChangeEvent;
import org.bricks.engine.staff.Walker;

public class ChangeSpeedProcessor <T extends Walker> extends ImmediateActProcessor<T>{

	private float speed;
	
	public ChangeSpeedProcessor(float speed){
		this.speed = speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	protected void processSingle(T target, long processTime) {
		SpeedChangeEvent.changeWalkerSpeed(target, speed);
	}


}
