package org.bricks.engine.neve;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.staff.Entity;

public class EntityPrint<E extends Entity> extends Imprint<E>{

	private final Ipoint origin  = new Ipoint(0, 0);
	
	public EntityPrint(PrintStore<E, ?> ps) {
		super(ps);
	}

	public Ipoint getOrigin(){
		return origin;
	}
	
	@Override
	protected void init() {
		Ipoint eOrigin = printStore.target.getOrigin();
		this.origin.setX(eOrigin.getX());
		this.origin.setY(eOrigin.getY());
	}

}
