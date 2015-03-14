package org.bricks.engine.staff;

import org.bricks.engine.tool.Origin;

public interface Satellite<C> {
	
	public void translate(Origin<C> vector);
	public void rotate(float rad, Origin<C> central);
	public void update();
}
