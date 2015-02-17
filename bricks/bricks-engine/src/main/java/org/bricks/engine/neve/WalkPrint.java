package org.bricks.engine.neve;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;

public class WalkPrint<P extends Walker<?>> extends RollPrint<P>{

	private final Fpoint vector = new Fpoint(0f, 0f);

	public WalkPrint(PrintStore<P, ?> ps) {
		super(ps);
	}


	public Fpoint getVector(){
		return vector;
	}
	
	@Override
	protected void init(){
		super.init();
		Fpoint eVector = this.getTarget().getVector();
		this.vector.setX(eVector.getFX());
		this.vector.setY(eVector.getFY());
	}
}
