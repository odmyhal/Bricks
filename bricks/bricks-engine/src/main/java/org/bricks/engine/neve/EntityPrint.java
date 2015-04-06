package org.bricks.engine.neve;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;

public class EntityPrint<E extends Entity, C> extends BasePrint<E>{

	private Origin<C> origin;//  = new Ipoint(0, 0);
	
	public EntityPrint(PrintStore<E, ?> ps) {
		super(ps);
		origin = printStore.target.provideInitialOrigin();
	}

	public Origin<C> getOrigin(){
		return origin;
	}
	
//	@Override
	public void init() {
		origin.set(printStore.target.origin());
/*		Ipoint eOrigin = printStore.target.getOrigin();
		this.origin.setX(eOrigin.getX());
		this.origin.setY(eOrigin.getY());*/
	}

}
