package org.bricks.extent.control;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.check.AccelerateToSpeedProcessorChecker;
import org.bricks.engine.staff.Walker;
import org.bricks.enterprise.control.widget.tool.FlowSlider;
import org.bricks.extent.entity.DefaultSpeedProvider;
import org.bricks.extent.entity.SpeedProvider;

public class AccelerateToSpeedEntityAction<T extends Walker<?, Fpoint>> extends EventCheckRegAction<T, FlowSlider> {
	
	private AccelerateToSpeedProcessorChecker<T> accProcessor;
	private SpeedProvider speedProvider;
	private float acceleration;

	public AccelerateToSpeedEntityAction(T target, float acceleration) {
		super(target);
		this.acceleration = acceleration;
		this.speedProvider = new DefaultSpeedProvider(target);
		accProcessor = new AccelerateToSpeedProcessorChecker<T>();
	}

	@Override
	public void init(FlowSlider widget) {
		float targetSpeed = widget.getValue();
		float speedDiff = targetSpeed - speedProvider.provideDirectionalSpeed();
		float accelerate = acceleration;
		if(speedDiff < 0){
			accelerate *= -1;
		}
		accProcessor.init(accelerate, targetSpeed);
		this.addChecker(accProcessor);
	}

}
