package org.bricks.engine.event.check;

import org.bricks.engine.staff.Liver;

public abstract class ImmediateEventChecker<T extends Liver<?>> extends SingleEventChecker<T>{

	public ImmediateEventChecker() {
		super(CheckerType.NO_SUPLANT);
	}

	@Override
	protected boolean ready(T target) {
		return true;
	}
}
