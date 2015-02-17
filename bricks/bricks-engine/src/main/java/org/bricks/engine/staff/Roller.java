package org.bricks.engine.staff;

import org.bricks.engine.neve.RollPrint;

public interface Roller<I extends RollPrint> extends Liver<I>{

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
