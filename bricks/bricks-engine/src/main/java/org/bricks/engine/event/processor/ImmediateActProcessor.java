package org.bricks.engine.event.processor;

import org.bricks.engine.staff.Liver;

public abstract class ImmediateActProcessor<T extends Liver> extends SingleActProcessor<T> {

	@Override
	protected boolean ready(T target, long processTime) {
		return true;
	}

}
