package org.bricks.extent.space;

import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;

public interface SpaceVehicle<C, R extends Roll> {

	public void translate(Origin<C> vector);
	public void rotate(R roll, Origin<C> central);
}
