package org.bricks.engine.neve;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;

public class WalkPrint<P extends Walker, C> extends RollPrint<P>{

	private final Origin<C> vector;// = new Fpoint(0f, 0f);

	public WalkPrint(PrintStore<P, ?> ps) {
		super(ps);
		vector = printStore.target.provideInitialOrigin();
	}


	public Origin<C> getVector(){
		return vector;
	}
	
	@Override
	protected void init(){
		super.init();
		vector.set(printStore.target.getVector());
/*		Fpoint eVector = this.getTarget().getVector();
		this.vector.setX(eVector.getFX());
		this.vector.setY(eVector.getFY());*/
	}
}
