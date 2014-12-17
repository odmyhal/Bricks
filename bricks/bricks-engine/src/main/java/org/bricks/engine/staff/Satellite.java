package org.bricks.engine.staff;

import org.bricks.core.entity.Point;

public interface Satellite {
	
	public void translate(int x, int y);
	public void rotate(float rad, Point central);
	public void update();
}
