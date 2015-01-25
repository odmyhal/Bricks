package org.bricks.engine.event.check;

import org.bricks.engine.staff.Liver;

public abstract class ImmediateEventChecker<T extends Liver> extends SingleEventChecker<T>{

	@Override
	protected boolean ready(Liver target) {
		return true;
	}
}
