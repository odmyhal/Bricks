package org.bricks.engine.processor;

import org.bricks.engine.event.control.RotationSpeedEvent;
import org.bricks.engine.processor.ImmediateActProcessor;
import org.bricks.engine.staff.Roller;

public class ChangeRotationSpeedProcessor<T extends Roller> extends ImmediateActProcessor<T> {
	
	private float rotationSpeed;
	
	public ChangeRotationSpeedProcessor(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}
	
	public void setRotationSpeed(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	protected void processSingle(T target, long processTime) {
		RotationSpeedEvent.changeRollerRotationSpeed(target, processTime, rotationSpeed);
	}

}
