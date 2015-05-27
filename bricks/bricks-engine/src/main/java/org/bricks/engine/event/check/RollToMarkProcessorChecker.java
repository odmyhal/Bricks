package org.bricks.engine.event.check;

import org.bricks.engine.event.control.RotationSpeedEvent;
import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.processor.ChangeRotationSpeedProcessor;
import org.bricks.engine.processor.ImmediateActProcessor;
import org.bricks.engine.processor.SingleActProcessor;
import org.bricks.engine.staff.Roller;
import org.bricks.exception.Validate;

public class RollToMarkProcessorChecker<T extends Roller> extends ChunkEventChecker<T>{

	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	public static final float rotationCycle = (float) (Math.PI * 2);
	
	private ChangeRotationSpeedProcessor<T> rotationSpeedProcessor;
	private ConditionalRotationProcessor conditionalRotationProcessor;
//	private volatile boolean inited = false;
	
	private float finishSpeed, targetRotation;
	//one of startSpeed, finishSpeed, targetRotation should be volatile
	private volatile float startSpeed;
	
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
		
	}
	
	/*
	 * Usually called from action in render thread
	 * inited is volatile to be read in motor thread
	 */
	public void init(float targetRotation, float startSpeed, float finishSpeed){
		this.targetRotation = targetRotation;
		this.finishSpeed = finishSpeed;
		/*
		 * Flush cached values via volatile
		 */
		this.startSpeed = startSpeed;
//		inited = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bricks.engine.event.check.ChunkEventChecker#activate()
	 * Called in motor thread
	 * inited is volatile because of it is changed in render thread
	 */
	@Override
	public void activate(T target, long curTime){
//		Validate.isTrue(inited, "Volatile inited must be true");
//		inited = false;
		/*
		 * Flush cached values via volatile
		 */
		rotationSpeedProcessor.setRotationSpeed(startSpeed);
		conditionalRotationProcessor.initialize(targetRotation, finishSpeed);
		super.activate(target, curTime);
	}
	
	private class ConditionalRotationProcessor extends SingleActProcessor<T>{
		
		public ConditionalRotationProcessor() {
			super(CheckerType.NO_SUPLANT);
			// TODO Auto-generated constructor stub
		}

		private float tRotation, fSpeed;
		
		public void initialize(float tRotation, float fSpeed){
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
//			boolean stopAction = false;
			float curSpeed = target.getRotationSpeed();
			if(curSpeed == 0f){
				System.out.println("WARN: " + this.getClass().getCanonicalName() + " checker will never provide event");
				target.unregisterEventChecker(this);
			}
			return RotationHelper.isRotationFinished(curSpeed, target.getRotation(), tRotation);
		}

		@Override
		protected void processSingle(T target, long processTime) {
			RotationSpeedEvent.changeRollerRotationSpeed(target, processTime, fSpeed);
		}
	}
}
