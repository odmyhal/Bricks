package org.bricks.engine.neve;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;

public class WalkPrint<P extends Walker, C> extends RollPrint<P, C>{

	private final Origin<C> vector;

	public WalkPrint(PrintStore<P, ?> ps) {
		super(ps);
		vector = printStore.target.provideInitialOrigin();
	}


	public Origin<C> getVector(){
		return vector;
	}
	
	@Override
	public void init(){
		super.init();
		vector.set(printStore.target.getVector());
	}
}
