package org.bricks.engine.processor;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.control.SpeedChangeEvent;
import org.bricks.engine.processor.ImmediateActProcessor;
import org.bricks.engine.staff.Walker;

public class ChangeSpeedProcessor <T extends Walker<?, Fpoint>> extends ImmediateActProcessor<T>{

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
