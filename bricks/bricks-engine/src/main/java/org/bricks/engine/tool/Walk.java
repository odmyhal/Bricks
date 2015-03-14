package org.bricks.engine.tool;

public interface Walk<C> {

	public boolean move(long time, C trn);
	
	public void flushTimer(long time);
	
	public boolean moveBack(long checkTime);
}
