package org.bricks.engine.staff;

import org.bricks.core.entity.Fpoint;

public interface Walker extends Roller{

	public void setVector(Fpoint vector);
	public void setVector(float x, float y);
	public float getAcceleration();
	public void setAcceleration(float acceleration, long accTime);
	public Fpoint getVector();
	public void translateNoView(int x, int y);
	
}
