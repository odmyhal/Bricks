package org.bricks.engine.tool;

import org.bricks.engine.staff.Walker;

public abstract class Walk<C> {
	
//	protected Walker<?, C> owner;
	protected Origin<C> lastMove;

	protected static final int moveLimit = 14;
	
	public Walk(/*Walker<?, C> walker*/){
//		this.owner = walker;
		lastMove = initLastMoveOrigin();
	}
	
	protected abstract Origin<C> initLastMoveOrigin();

	public abstract boolean move(long time, C trn);
	
	public abstract void flushTimer(long time);
	
	public abstract boolean moveBack(long checkTime, float k);
	
	public Origin<C> lastMove(){
		return lastMove;
	}
}
