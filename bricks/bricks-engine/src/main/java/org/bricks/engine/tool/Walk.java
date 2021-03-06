package org.bricks.engine.tool;

import org.bricks.engine.Engine;
import org.bricks.engine.staff.AvareTimer;
import org.bricks.engine.staff.Walker;

public abstract class Walk<C> implements AvareTimer{
	
//	protected Walker<?, C> owner;
	protected Origin<C> lastMove;

	protected static final int moveLimit = Engine.preferences.getInt("walk.motion.limit", 100);
	
	public Walk(/*Walker<?, C> walker*/){
//		this.owner = walker;
		lastMove = initLastMoveOrigin();
	}
	
	protected abstract Origin<C> initLastMoveOrigin();

	public abstract boolean move(long time, C trn);
	
//	public abstract void flushTimer(long time);
	
	public abstract boolean moveBack(long checkTime, float k);
	
	public Origin<C> lastMove(){
		return lastMove;
	}
}
