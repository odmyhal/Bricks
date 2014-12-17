package org.bricks.exception;

import org.bricks.core.entity.type.Brick;

public class BricksException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Brick brick;
	
	public BricksException(Brick brick, String msg){
		super("Error: " + msg + " / Source Class: " + brick.getClass().getCanonicalName() + " / Instance: " + brick);
		this.brick = brick;
	}

	public Brick getBrick() {
		return brick;
	}
}
