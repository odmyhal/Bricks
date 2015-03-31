package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.BaseSubject;

public interface OverlapAlgorithm<T extends Imprint<? extends BaseSubject>, K extends Imprint<? extends BaseSubject>, P> {

	public OverlapEvent<T, K, P> checkOverlap(T target, K client);
	
	public P findOverlapPoint(T target, K client);
	
	public boolean isOvarlap(T target, K client);
}
