package org.bricks.engine.tool;

import org.bricks.exception.Validate;

public class Roll {
	
	private static final float rotationCicle = (float) (Math.PI * 2);

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
			if(rotation > rotationCicle){
				rotation -= rotationCicle;
			}else if(rotation < 0){
				rotation += rotationCicle;
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
		lastRotation = 0;
		rotateTime = checkTime;
		return true;
	}
}
