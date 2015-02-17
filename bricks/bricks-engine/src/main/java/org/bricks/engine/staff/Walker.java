package org.bricks.engine.staff;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.neve.WalkPrint;

public interface Walker<I extends WalkPrint> extends Roller<I>{

	public void setVector(Fpoint vector);
	public void setVector(float x, float y);
	public float getAcceleration();
	public void setAcceleration(float acceleration, long accTime);
	public Fpoint getVector();
	public void translateNoView(int x, int y);
	
}
