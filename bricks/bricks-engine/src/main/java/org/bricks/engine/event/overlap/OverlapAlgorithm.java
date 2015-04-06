package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.staff.Subject;

public interface OverlapAlgorithm<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject>, P> {

	public OverlapEvent<T, K, P> checkOverlap(T target, K client);
	
	public P findOverlapPoint(T target, K client);
	
	public boolean isOvarlap(T target, K client);
}
