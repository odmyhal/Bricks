package org.bricks.engine.neve;

import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.engine.pool.BrickSubject;

public class EntityPointsPrint<P extends BrickSubject, EP extends EntityPrint> extends PointSetPrint<P> {
	
	public EP entityPrint;

	public EntityPointsPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	@Override
	protected void init(){
		super.init();
		entityPrint = (EP) getTarget().getEntity().getSafePrint();
	}
	
	protected void endUse(){
		entityPrint.free();
		entityPrint = null;
		super.endUse();
	}
}
