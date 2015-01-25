package org.bricks.engine.event.check;

import org.bricks.engine.event.control.RotationSpeedEvent;
import org.bricks.engine.event.processor.ChangeRotationSpeedProcessor;
import org.bricks.engine.event.processor.ImmediateActProcessor;
import org.bricks.engine.event.processor.SingleActProcessor;
import org.bricks.engine.staff.Roller;

public class RollToMarkProcessorChecker<T extends Roller> extends ChunkEventChecker<T>{

	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	private static final float rotationCycle = (float) (Math.PI * 2);
	
	private ChangeRotationSpeedProcessor<T> rotationSpeedProcessor;
	private ConditionalRotationProcessor conditionalRotationProcessor;
	private volatile float startSpeed, finishSpeed, targetRotation;
	
	public RollToMarkProcessorChecker(){
		this(0f, 0f, 0f);
	}
	
	public RollToMarkProcessorChecker(float targetRotation, float startSpeed){
		this(targetRotation, startSpeed, 0f);
	}
	
	public RollToMarkProcessorChecker(final float targetRotation, final float startSpeed, final float finishSpeed){
		super(CHECKER_TYPE);
		this.supplant(CHECKER_TYPE);
		
		init(targetRotation, startSpeed, finishSpeed);

		rotationSpeedProcessor = new ChangeRotationSpeedProcessor(startSpeed);
		this.addProcessor(rotationSpeedProcessor);
		
		conditionalRotationProcessor = new ConditionalRotationProcessor();
		this.addProcessor(conditionalRotationProcessor);
		
		System.out.println(this.getClass().getCanonicalName() + " created...");
	}
	
	public void init(float targetRotation, float startSpeed, float finishSpeed){
		this.targetRotation = targetRotation;
		this.startSpeed = startSpeed;
		this.finishSpeed = finishSpeed;
	}
	
	@Override
	public void activate(){
		rotationSpeedProcessor.setRotationSpeed(startSpeed);
		conditionalRotationProcessor.initialize(targetRotation, finishSpeed);
		super.activate();
	}
	
	private class ConditionalRotationProcessor extends SingleActProcessor<T>{
		
		private float tRotation, fSpeed;
		
		private void initialize(float tRotation, float fSpeed){
			this.tRotation = tRotation;
			this.fSpeed = fSpeed;
		}
		
		/**
		 * target.getRotation() value is [0, 2pi)
		 * if (curSpeed > 0) tRotation should be bigger than target.getRotation(), otherwise checker stops
		 * if (curSpeed < 0) tRotation should be smaller than target.getRotation(), otherwise checker stops
		 */
		@Override
		protected boolean ready(T target, long processTime) {
			boolean stopAction = false;
			float curSpeed = target.getRotationSpeed();
			if(curSpeed == 0f){
				System.out.println("WARN: " + this.getClass().getCanonicalName() + " checker will never provide event");
				target.unregisterEventChecker(this);
			}
			float diffRad = tRotation - target.getRotation();
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
		protected void processSingle(T target, long processTime) {
			RotationSpeedEvent.changeRollerRotationSpeed(target, processTime, fSpeed);
		}
	}
}
