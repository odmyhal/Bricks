package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.control.AccelerationChangeEvent;
import org.bricks.engine.event.control.SpeedChangeEvent;
import org.bricks.engine.staff.Walker;
import org.bricks.exception.Validate;

/**
 * @deprecated use AcceleratoToSpeedProceesorChecker
 * @author oleh
 *
 * @param <T>
 */
@Deprecated
public class AccelerateToSpeedChecker<T extends Walker> extends ChunkEventChecker<T> {
	
	

	public AccelerateToSpeedChecker(final float accelerate, final float targetSpeed){
		
		Validate.isFalse(accelerate == 0, "Adding checker with zero accelerate has no sense, and can be infinitelly");
		
		this.supplant(AccelerateToSpeedProcessorChecker.CHECKER_TYPE);
		
		this.addChecker(new ImmediateEventChecker<T>(){

			@Override
			protected Event produceEvent(T target) {
				return new AccelerationChangeEvent(accelerate);
			}
			
		});
		
		this.addChecker(new SingleEventChecker<T>(){

			@Override
			protected boolean ready(T target) {
				float vx = target.getVector().getFX();
				float vy = target.getVector().getFY();
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
						Validate.isFalse(mark2 == 0, "Considered unreal combination");
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
			protected Event produceEvent(T target) {
				return new AccelerationChangeEvent(0f);
			}
			
		});
		
		this.addChecker(new ImmediateEventChecker<T>(){

			@Override
			protected Event produceEvent(T target) {
				return new SpeedChangeEvent(targetSpeed);
			}
			
		});
	}
}
