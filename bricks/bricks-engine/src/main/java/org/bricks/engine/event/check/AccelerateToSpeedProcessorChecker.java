package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.control.SpeedChangeEvent;
import org.bricks.engine.processor.ChangeAccelerationProcessor;
import org.bricks.engine.processor.ChangeSpeedProcessor;
import org.bricks.engine.processor.ImmediateActProcessor;
import org.bricks.engine.processor.SingleActProcessor;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Origin2D;
import org.bricks.exception.Validate;

public class AccelerateToSpeedProcessorChecker<T extends Walker<?, Fpoint>> extends ChunkEventChecker<T> {
	
	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	
	private ChangeAccelerationProcessor<T> changeAccelerationProcessor;
	private ChangeSpeedProcessor<T> changeSpeedProcessor;
	private ConditionalAccDisableProcessor accDisableProcessor;
	private float speed;
	//One of speed & directAcceleration should be volatile
	private volatile float directAcceleration;
//	private volatile boolean inited = false;
	private Origin<Fpoint> tmpAcceleration = new Origin2D();
	
	public AccelerateToSpeedProcessorChecker(){
		this(1f, 0f);
	}

	public AccelerateToSpeedProcessorChecker(final float accelerate, final float targetSpeed){
		super(CHECKER_TYPE);
		init(accelerate, targetSpeed);
		
		this.addSupplant(CHECKER_TYPE);
		
		changeAccelerationProcessor = new ChangeAccelerationProcessor(new Origin2D());
		this.addProcessor(changeAccelerationProcessor);
		
		accDisableProcessor = new ConditionalAccDisableProcessor();
		this.addProcessor(accDisableProcessor);
		
		changeSpeedProcessor = new ChangeSpeedProcessor(targetSpeed);
		this.addProcessor(changeSpeedProcessor);
		
	}
	
	public void init(final float accelerate, final float targetSpeed){
		Validate.isFalse(accelerate == 0, "Adding checker with zero accelerate has no sense, and can be infinitelly");
		speed = targetSpeed;
		/*
		 * Flush cached values via volatile
		 */
		directAcceleration = accelerate;
//		inited = true;
	}
	
	public void activate(T target, long curTime){
//		Validate.isTrue(inited, "Volatile inited should be true");
//		inited = false;
		/*
		 * Flush cached values via volatile
		 */
		float curAcceleration = directAcceleration;
		changeAccelerationProcessor.setAcceleration(applyDirectAcceleration(target, curAcceleration));
		changeSpeedProcessor.setSpeed(speed);
		accDisableProcessor.initialize(curAcceleration, speed);
		super.activate(target, curTime);
	}
	
	private Origin<Fpoint> applyDirectAcceleration(T target, float curAcceleration){
		/**
		 * flushes cached values via volation directAcceleration
		 */
		double rotation = target.getRotation();
		tmpAcceleration.source.x = curAcceleration * (float) Math.cos(rotation);
		tmpAcceleration.source.y = curAcceleration * (float) Math.sin(rotation);
		return tmpAcceleration;
	}
	
	private class ConditionalAccDisableProcessor extends SingleActProcessor<T>{
		
		public ConditionalAccDisableProcessor() {
			super(CheckerType.NO_SUPLANT);
		}

		private float accelerate, targetSpeed;
		
		private void initialize(float accelerate, float targetSpeed){
			this.accelerate = accelerate;
			this.targetSpeed = targetSpeed;
		}
		
		@Override
		protected boolean ready(T target, long processTime) {
			Fpoint vector = target.getVector().source;
			float vx = vector.x;//.getFX();
			float vy = vector.y;//.getFY();
			double curSpeed = Math.sqrt(vx * vx + vy * vy);
			if(curSpeed != 0){
				double mark1 = vx * Math.cos((double) target.getRotation());
				if(mark1 < 0){
					curSpeed *= -1;
				}else if(mark1 == 0){
					double mark2 = vy * Math.sin((double) target.getRotation());
					if(mark2 < 0){
						curSpeed *= -1;
					}
					Validate.isFalse(mark2 == 0, "Assumed unreal combination");
				}
			}
			if(accelerate > 0 && targetSpeed <= curSpeed){
				return true;
			}
			if(accelerate < 0 && targetSpeed >= curSpeed){
				return true;
			}
			return false;
		}

		@Override
		protected void processSingle(T target, long processTime) {
			tmpAcceleration.source.x = 0f;
			tmpAcceleration.source.y = 0f;
			target.setAcceleration(tmpAcceleration, processTime);
		}
	}
}
