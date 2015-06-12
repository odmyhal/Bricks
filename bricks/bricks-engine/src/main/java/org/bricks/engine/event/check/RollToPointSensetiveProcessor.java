package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.item.MultiRoller;
import org.bricks.core.help.AlgebraHelper;

public class RollToPointSensetiveProcessor<K extends MultiRoller<?, ?, Fpoint, ?>> extends RollToPointProcessorChecker<K>{
	
	private double qSensitiveDistance;

	public RollToPointSensetiveProcessor(Fpoint targetDirectionPoint,
			float startRotationSpeed, float sensitiveDistance) {
		super(targetDirectionPoint, startRotationSpeed);
		this.qSensitiveDistance = sensitiveDistance * sensitiveDistance;
	}
	
	protected ConditionalRotationProcessor conditionalRotationProcessor(Fpoint targetDirectionPoint){
		return new ConditionalSensitiveRotationProcessor(targetDirectionPoint);
	}
	
	protected class ConditionalSensitiveRotationProcessor extends RollToPointProcessorChecker<K>.ConditionalRotationProcessor{

		public ConditionalSensitiveRotationProcessor(Fpoint dp) {
			super(dp);
		}
		
		@Override
		protected boolean ready(K target, long processTime) {
			Fpoint targetOrigin = target.origin().source;
			float qDistance = AlgebraHelper.pow(targetOrigin.x - this.directionPoint.x, 2)
					+ AlgebraHelper.pow(targetOrigin.y - this.directionPoint.y, 2);
			if(qDistance < qSensitiveDistance){
				return true;
			}
			return super.ready(target, processTime);
		}
		
	}
}
