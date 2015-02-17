package org.bricks.engine.neve;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Logable;

public class SubjectPrint<P extends Subject, EP extends EntityPrint> extends PointSetPrint<P>{
	
	public EP entityPrint;

	public SubjectPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

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
