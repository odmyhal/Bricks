package org.bricks.engine.event.processor;

import org.bricks.engine.staff.Walker;

public class ChangeAccelerationProcessor<T extends Walker> extends ImmediateActProcessor<T>{
	
	private float acceleration;
	
	public ChangeAccelerationProcessor(float acceleration){
		this.acceleration = acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	@Override
	protected void processSingle(T target, long processTime) {
		target.setAcceleration(acceleration, processTime);
	}

}
