package org.bricks.exception;

import org.bricks.core.entity.type.Brick;

public class BrickInitException extends BricksException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BrickInitException(Brick brick){
		super(brick, "Could not initialize Brick");
	}

}
