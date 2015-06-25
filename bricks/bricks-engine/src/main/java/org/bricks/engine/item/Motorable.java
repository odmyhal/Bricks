package org.bricks.engine.item;

import org.bricks.engine.staff.AvareTimer;

public interface Motorable extends AvareTimer{

	public void motorProcess(long currentTime);
	public boolean alive();
}
