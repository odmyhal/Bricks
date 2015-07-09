package org.bricks.engine.event.overlap;

public interface OverlapAlgorithm<T, S, P> {

//	public P checkOverlap(T targetData, S clientData);
	
	public P findOverlapPoint(T targetData, S clientData);
	
	public boolean isOvarlap(T targetData, S clientData);
}
