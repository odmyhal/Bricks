package org.bricks.engine.staff;

import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;

public interface Satellite<C, R extends Roll> {
	
	public void translate(Origin<C> vector);
	public void rotate(R roll, Origin<C> central);
	public void update();
}
