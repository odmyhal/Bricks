package org.bricks.engine.staff;

public interface Roller extends Liver{

	public float getRotationSpeed();
	public void setRotationSpeed(float rotationSpeed);
	public float getRotation();
	public void setRotation(float radians);
//	public void resetMoveTime();
	public void flushTimer(long nTime);
	public boolean rotate(long checkTime);
	public void rollBack(long currentTime);
	
	public void applyRotation();
}
