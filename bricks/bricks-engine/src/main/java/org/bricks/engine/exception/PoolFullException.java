package org.bricks.engine.exception;

import org.bricks.engine.pool.Pool;

public class PoolFullException extends BricksEngineException {

	public PoolFullException(Pool pool){
		super("Pool is full / class: " + pool.getClass().getCanonicalName() + " / capacity:" + pool.capacity());
	}
}
