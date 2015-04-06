package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.Subject;

public abstract class BaseOverlapAlgorithm<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject>, P> implements OverlapAlgorithm<T, K, P> {

	public OverlapEvent<T, K, P> checkOverlap(T target, K client) {
		P touchPoint = findOverlapPoint(target, client);
		if(touchPoint == null){
			return null;
		}
		return new OverlapEvent<T, K, P>(target, client, touchPoint);
	}

}
