package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.event.RotationSpeedEvent;

public class RollToMarkChecker extends SingleEventChecker<Roller>{

	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	private static final float rotationCicle = (float) (Math.PI * 2);
	
	private float newSpeed = 0f, targetRotation;
	private String eventSource;

	public RollToMarkChecker(float targetRotation){
		this(targetRotation, RotationSpeedEvent.ROTATION_EV_SOURCE);
	}
	
	public RollToMarkChecker(float targetRotation, String eventSource){
		super(CHECKER_TYPE);
		this.targetRotation = targetRotation;
		this.eventSource = eventSource;
		this.supplant(CHECKER_TYPE);
	}
	
	public RollToMarkChecker(float newSpeed, float targetRotation){
		this(newSpeed, targetRotation, RotationSpeedEvent.ROTATION_EV_SOURCE);
	}
	
	public RollToMarkChecker(float newSpeed, float targetRotation, String eventSource){
		this.newSpeed = newSpeed;
		this.targetRotation = targetRotation;
		this.eventSource = eventSource;
	}
	
	@Override
	protected boolean ready(Roller target) {
		boolean stopAction = false;
		float curSpeed = target.getRotationSpeed();
		if(curSpeed == 0f){
			System.out.println("WARN: " + this.getClass().getCanonicalName() + " checker will never provide event");
			target.unregisterEventChecker(this);
		}
		float curRad = target.getRotation();
		while(curRad > rotationCicle){
			curRad -= rotationCicle;
		}
		while(curRad <= -rotationCicle){
			curRad += rotationCicle;
		}
		float diffRad = targetRotation - curRad;
		if(curSpeed >= 0){
			if(diffRad <= 0 && diffRad > -Math.PI){
				stopAction = true;
			}
		}
		if(curSpeed < 0){
			if(diffRad >= 0 && diffRad < Math.PI){
				stopAction = true;
			}
		}
		return stopAction;
	}

	@Override
	protected Event produceEvent(Roller target) {
		return new RotationSpeedEvent(newSpeed, eventSource);
	}

}
