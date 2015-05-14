package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.Subject;

public abstract class BaseOverlapAlgorithm<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject>, P> implements OverlapAlgorithm<T, K, P> {

//	private int cnt = 0;
//	private int logCnt = 0;
	public OverlapEvent<T, K, P> checkOverlap(T target, K client) {
/*		if(++cnt > 10000){
			System.out.println("**" + (++logCnt) + " Check " + target.getTarget().getEntity() + " for " + client.getTarget().getEntity());
			System.out.println("----- " + ((PlanePointsPrint)target).getCenter() + " for " + ((PlanePointsPrint)client).getCenter());
			cnt = 0;
		}*/
		P touchPoint = findOverlapPoint(target, client);
		if(touchPoint == null){
			return null;
		}
//		System.out.println("**Found overlap point " + target.getTarget().getEntity() + " for " + client.getTarget().getEntity());
		return new OverlapEvent<T, K, P>(target, client, touchPoint);
	}

}
