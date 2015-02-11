package org.bricks.engine.event.processor;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.staff.Liver;

public abstract class ImmediateActProcessor<T extends Liver> extends SingleActProcessor<T> {

	public ImmediateActProcessor() {
		super(CheckerType.NO_SUPLANT);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean ready(T target, long processTime) {
		return true;
	}

}
