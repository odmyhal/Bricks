package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.event.control.RotationSpeedEvent;

/**
 * @deprecated use RollToMarkProcessorChecker instead...
 * @author oleh
 *
 */
@Deprecated
public class RollToMarkChecker extends DoubleEventChecker<Roller>{

//	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	private static final float rotationCycle = (float) (Math.PI * 2);
	
	private float startSpeed, finishSpeed, targetRotation;
	private String eventSource;

	/**
	 * At least targetRotation and startSpeed must be specified
	 * @param targetRotation
	 * @param startSpeed
	 */
	public RollToMarkChecker(float targetRotation, float startSpeed){
		this(targetRotation, startSpeed, 0f, RotationSpeedEvent.ROTATION_EV_SOURCE);
	}
	
	public RollToMarkChecker(float targetRotation, float startSpeed, String eventSource){
		this(targetRotation, startSpeed, 0f, eventSource);
	}
	
	public RollToMarkChecker(float targetRotation, float startSpeed, float finishSpeed){
		this(targetRotation, startSpeed, finishSpeed, RotationSpeedEvent.ROTATION_EV_SOURCE);
	}
	
	public RollToMarkChecker(float targetRotation, float startSpeed, float finishSpeed, String eventSource){
		super(RollToMarkProcessorChecker.CHECKER_TYPE);
		this.startSpeed = startSpeed;
		this.finishSpeed = finishSpeed;
		this.targetRotation = targetRotation;
		this.eventSource = eventSource;
		this.supplant(RollToMarkProcessorChecker.CHECKER_TYPE);
	}
	
	/**
	 * currentRotaion value is [0, 2pi)
	 * if (curSpeed > 0) targetRotation should be bigger than currentRotation, otherwise checker stops
	 * if (curSpeed < 0) targetRotation should be smaller than currentRotation, otherwise checker stops
	 */
	@Override
	protected boolean ready(Roller target) {
		boolean stopAction = false;
		float curSpeed = target.getRotationSpeed();
		if(curSpeed == 0f){
			System.out.println("WARN: " + this.getClass().getCanonicalName() + " checker will never provide event");
			target.unregisterEventChecker(this);
		}
		float diffRad = targetRotation - target.getRotation();
		if(curSpeed >= 0){
			if(diffRad >= Math.PI){
				diffRad -= rotationCycle;
			}
			if(diffRad <= 0){
				stopAction = true;
			}
		}
		if(curSpeed < 0){
			if(diffRad <= -Math.PI){
				diffRad += rotationCycle;
			}
			if(diffRad >= 0){
				stopAction = true;
			}
		}
		return stopAction;
	}
	
	@Override
	protected Event produceStartEvent(Roller target) {
		return new RotationSpeedEvent(startSpeed, eventSource);
	}

	@Override
	protected Event produceFinishEvent(Roller target) {
		return new RotationSpeedEvent(finishSpeed, eventSource);
	}

}
