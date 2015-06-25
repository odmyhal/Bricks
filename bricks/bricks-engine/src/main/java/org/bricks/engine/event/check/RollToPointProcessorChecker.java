package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.control.RotationSpeedEvent;
import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.processor.ChangeRotationSpeedProcessor;
import org.bricks.engine.processor.SingleActProcessor;
import org.bricks.engine.staff.Roller;
import org.bricks.exception.Validate;

public class RollToPointProcessorChecker<T extends MultiRoller<?, ?, Fpoint, ?>> extends ChunkEventChecker<T> {
	
	private ChangeRotationSpeedProcessor<T> rotationSpeedProcessor;
	private ConditionalRotationProcessor conditionalRotationProcessor;
	
//	private float finishSpeed;
	private volatile float startSpeed;
	private Fpoint targetDirection = new Fpoint(), hPoint = new Fpoint();
	

	public RollToPointProcessorChecker(Fpoint targetDirectionPoint, float startRotationSpeed) {
		super(RollToMarkProcessorChecker.CHECKER_TYPE);
		this.addSupplant(RollToMarkProcessorChecker.CHECKER_TYPE);
		
		this.init(targetDirectionPoint, startRotationSpeed);
		
		rotationSpeedProcessor = new ChangeRotationSpeedProcessor(startSpeed);
		this.addProcessor(rotationSpeedProcessor);
		
		conditionalRotationProcessor = conditionalRotationProcessor(targetDirectionPoint);
		this.addProcessor(conditionalRotationProcessor);
	}

	private void init(Point targetDirectionPoint, float startRotationSpeed){
		this.targetDirection.set(targetDirectionPoint.getFX(), targetDirectionPoint.getFY());
		this.startSpeed = startRotationSpeed;
	}
	
	@Override
	public void activate(T target, long curTime){
		float sSpeed = startSpeed;
		Validate.isFalse(sSpeed == 0);
		/*
		 * Flush cached values via volatile
		 */
		
		
		Fpoint curOrigin = target.origin().source;
		hPoint.set(targetDirection.x - curOrigin.x, targetDirection.y - curOrigin.y);
		PointHelper.normalize(hPoint);
		float curRotation = target.getRotation();
		float targetRotation = (float) AlgebraHelper.trigToRadians(hPoint.x, hPoint.y);
		
		float rotationDiff = targetRotation - curRotation;
		if(rotationDiff > Math.PI || ( rotationDiff < 0 && rotationDiff > -Math.PI) ){
			sSpeed *= -1;
		}
		
		rotationSpeedProcessor.setRotationSpeed(sSpeed);
//		conditionalRotationProcessor.initialize(curRotation, targetRotation);
		super.activate(target, curTime);
	}
	
	protected ConditionalRotationProcessor conditionalRotationProcessor(Fpoint targetDirectionPoint){
		return new ConditionalRotationProcessor(targetDirectionPoint);
	}
	
	protected class ConditionalRotationProcessor extends SingleActProcessor<T>{
		
		protected Fpoint directionPoint;
		private Fpoint hPoint = new Fpoint();
//		private float previousTarget, previousRotation;
		
		public ConditionalRotationProcessor(Fpoint dp) {
			super(CheckerType.NO_SUPLANT);
			this.directionPoint = dp;
		}

		/**
		 * target.getRotation() value is [0, 2pi)
		 * if (curSpeed > 0) tRotation should be bigger than target.getRotation(), otherwise checker stops
		 * if (curSpeed < 0) tRotation should be smaller than target.getRotation(), otherwise checker stops
		 */
//		@Override
		protected boolean ready(T target, long processTime) {
			boolean stopAction = false;
			float curSpeed = target.getRotationSpeed();
			Fpoint curOrigin = target.origin().source;
			hPoint.set(directionPoint.x - curOrigin.x, directionPoint.y - curOrigin.y);
			PointHelper.normalize(hPoint);
			float curRotation = target.getRotation();
			float targetRotation = (float) AlgebraHelper.trigToRadians(hPoint.x, hPoint.y);
//			float rememberTargetRotation = targetRotation;
			float diff = targetRotation - curRotation;
			if(curSpeed > 0){
				if((diff < 0 && diff > -Math.PI) || diff > Math.PI){
					stopAction = true;
				}
/*				if(previousTarget < previousRotation){
					previousTarget += RollToMarkProcessorChecker.rotationCycle;
					if(Math.abs(targetRotation - previousTarget) > Math.PI){
						targetRotation += RollToMarkProcessorChecker.rotationCycle;
					}
				}
				if(curRotation < previousRotation){
					curRotation += RollToMarkProcessorChecker.rotationCycle;
				}
				if(curRotation > targetRotation){
					stopAction = true;
				}*/
			}else if(curSpeed < 0){
				if((diff > 0 && diff < Math.PI) || diff < -Math.PI){
					stopAction = true;
				}
/*				if(previousTarget > previousRotation){
					previousTarget -= RollToMarkProcessorChecker.rotationCycle;
					if(Math.abs(targetRotation - previousTarget) > Math.PI){
						targetRotation -= RollToMarkProcessorChecker.rotationCycle;
					}
				}
				if(curRotation > previousRotation){
					curRotation -= RollToMarkProcessorChecker.rotationCycle;
				}
				if(curRotation < targetRotation){
					stopAction = true;
				}*/
			}else{
//				System.out.println("WARN: " + this.getClass().getCanonicalName() + " checker will never provide event");
				target.unregisterEventChecker(this);
			}
//			previousRotation = curRotation;
//			previousTarget = rememberTargetRotation;
			return stopAction;
		}
/*		
		private void initialize(float previousRotation, float previousTarget){
			this.previousRotation = previousRotation;
			this.previousTarget = previousTarget;
		}
*/
		@Override
		protected void processSingle(T target, long processTime) {
			RotationSpeedEvent.changeRollerRotationSpeed(target, processTime, 0f);
			
			Fpoint curOrigin = target.origin().source;
			hPoint.set(directionPoint.x - curOrigin.x, directionPoint.y - curOrigin.y);
			PointHelper.normalize(hPoint);
			float targetRotation = (float) AlgebraHelper.trigToRadians(hPoint.x, hPoint.y);
			target.setToRotation(targetRotation);
		}
	}

}
