package org.bricks.engine.staff;

import org.bricks.core.entity.Fpoint;

public interface Walker extends Roller{

	public void setVector(Fpoint vector);
	public void setVector(float x, float y);
	public Fpoint getVector();
	public void translateNoView(int x, int y);
	
}
