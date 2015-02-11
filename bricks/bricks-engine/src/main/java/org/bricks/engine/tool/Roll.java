package org.bricks.engine.tool;

import org.bricks.exception.Validate;

public class Roll {
	
	public static final float rotationCycle = (float) (Math.PI * 2);

	/**
	 * Rotation stored in range [0; 2*PI)
	 */
	private float rotation;
	private float rotationSpeed;
	private float rotationBuff = (float) (Math.PI / 180);
	private long rotateTime = System.currentTimeMillis();
	private float lastRotation;


	public float getRotationSpeed() {
		return rotationSpeed;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
	
	public float lastRotation(){
		return lastRotation;
	}

	public void flushTimer(long nTime){
		rotateTime = nTime;
	}

	public boolean rotate(long checkTime){
		return rotate(checkTime, rotationSpeed);
	}
	
	private boolean rotate(long checkTime, float rSpeed){
		boolean res = false;
		long diffR = checkTime - rotateTime;
		Validate.isTrue(diffR >= 0, " diff is " + diffR);
		int rMult = (int) ((diffR * rSpeed) / (1000 * rotationBuff));
		if(rMult != 0){
			res = true;
			lastRotation = rMult * rotationBuff;
			rotation += lastRotation;
			while(rotation >= rotationCycle){
				rotation -= rotationCycle;
			}
			while(rotation < 0){
				rotation += rotationCycle;
			}
			rotateTime += (int) (lastRotation * 1000 / rSpeed);
		}else{
			lastRotation = 0;
		}
		return res;
	}
	
	public boolean rotateBack(long checkTime){
		if(lastRotation == 0){
			return false;
		}
		Validate.isTrue(checkTime >= rotateTime);
		rotation -= lastRotation;
		/**
		 * controversial, need to be checked
		 */
		lastRotation = -lastRotation;
		rotateTime = checkTime;
		return true;
	}
}
