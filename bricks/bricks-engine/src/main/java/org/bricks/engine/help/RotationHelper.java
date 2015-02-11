package org.bricks.engine.help;

import org.bricks.exception.Validate;

public class RotationHelper {
	
	public static final float rotationCycle = (float) (Math.PI * 2);
	
	private static final ThreadLocal<RotationData> rotationData = new ThreadLocal<RotationData>(){
		@Override protected RotationData initialValue() {
            return new RotationData();
        }
	};

	/**
	 * 
	 * @param startRotationRad start rotation point should be [0, 2 * PI)
	 * @param targetRotationRad target rotation point should be [0, 2 * PI)
	 * @param rotationSpeed should be > 0
	 */
	public static final void calculateRotationData(float startRotationRad, float targetRotationRad, float rotationSpeed){
		Validate.isTrue(rotationSpeed > 0, "Rotation speed should be more than zero");
		float resSpeed, resTarget;
		resSpeed = rotationSpeed;
		float curDiffRad = targetRotationRad - startRotationRad;
		resTarget = targetRotationRad;
		if( curDiffRad > Math.PI || (curDiffRad < 0 && curDiffRad >= -Math.PI)){
			resSpeed *= -1;
			if(resTarget > startRotationRad){
				resTarget -= rotationCycle;
			}
		}else if(resTarget < startRotationRad){
			resTarget += rotationCycle;
		}
		rotationData.get().init(resSpeed, resTarget);
	}
	
	public static float getCalculatedRotationSpeed(){
		return rotationData.get().getCalculatedRotationSpeed();
	}
	
	public static float getCalculatedTargetRotation(){
		return rotationData.get().getCalculatedTargetRotation();
	}
	
	/**
	 * curRotation() value is [0, 2pi)
	 * if (rSpeed > 0) tarRotation should be bigger than curRotation, otherwise checker stops
	 * if (rSpeed < 0) tarRotation should be smaller than curRotation, otherwise checker stops
	 */
	public static boolean isRotationFinished(float rSpeed, float curRotation, float tarRotation){
		boolean finished = false;
		float diffRad = tarRotation - curRotation;
		if(rSpeed > 0){
			if(diffRad >= Math.PI){
				diffRad -= rotationCycle;
			}
			if(diffRad <= 0){
				finished = true;
			}
		}else if(rSpeed < 0){
			if(diffRad <= -Math.PI){
				diffRad += rotationCycle;
			}
			if(diffRad >= 0){
				finished = true;
			}
		}else{
			Validate.isTrue(true, "Rotation speed must not be zero!!!");
		}
		return finished;
	}
	
	private static class RotationData{
		
		private boolean speedMark, targetMark;
		private float calculatedRotationSpeed;
		private float calculatedTargetRotation;
		
		private void init(float speed, float target){
			speedMark = targetMark = true;
			calculatedRotationSpeed = speed;
			calculatedTargetRotation = target;
		}
		
		private float getCalculatedRotationSpeed(){
			Validate.isTrue(speedMark, "call calculateRotationData first");
			speedMark = false;
			return calculatedRotationSpeed;
		}
		
		private float getCalculatedTargetRotation(){
			Validate.isTrue(targetMark, "call calculateRotationData first");
			targetMark = false;
			return calculatedTargetRotation;
		}
	}
}
