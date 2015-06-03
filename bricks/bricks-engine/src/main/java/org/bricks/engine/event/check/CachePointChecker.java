package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.staff.Walker;

public abstract class CachePointChecker<T extends Walker<?, Fpoint>/*MultiWalker<?, ?, Fpoint, ?>*/> extends SingleEventChecker<T> {
	
	private Fpoint targetPoint = new Fpoint();
	private float sensitiveDistanceQuad;
	private Fpoint hPoint = new Fpoint();
	
	public CachePointChecker(Fpoint tPoint, float distance){
		this(CheckerType.NO_SUPLANT, tPoint, distance);
	}
	
	public CachePointChecker(CheckerType chType, Fpoint tPoint, float distance){
		super(chType);
		init(tPoint, distance);
	}

	public void init(Fpoint tPoint, float sDistance){
		targetPoint.set(tPoint.x, tPoint.y);
		sensitiveDistanceQuad = sDistance * sDistance;
	}

	@Override
	protected boolean ready(T target) {
		Fpoint origin = target.origin().source;
		hPoint.set(origin.x - targetPoint.x, origin.y - targetPoint.y);
		return this.sensitiveDistanceQuad > hPoint.x * hPoint.x + hPoint.y * hPoint.y;
	}
	
}
