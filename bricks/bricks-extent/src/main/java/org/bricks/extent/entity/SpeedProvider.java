package org.bricks.extent.entity;

import org.bricks.core.entity.Fpoint;

public interface SpeedProvider {
	
	public Fpoint provideSpeed();
	
	public float provideDirectionalSpeed();
}
