package org.bricks.engine.processor;

import org.bricks.engine.processor.ImmediateActProcessor;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;

public class ChangeAccelerationProcessor<T extends Walker> extends ImmediateActProcessor<T>{
	
	private Origin acceleration;
	
	public ChangeAccelerationProcessor(Origin acceleration){
		this.acceleration = acceleration;
	}

	public void setAcceleration(Origin acceleration) {
		this.acceleration.set(acceleration);
	}

	@Override
	protected void processSingle(T target, long processTime) {
		target.setAcceleration(acceleration, processTime);
	}

}
