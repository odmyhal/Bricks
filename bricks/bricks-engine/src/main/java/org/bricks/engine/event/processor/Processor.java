package org.bricks.engine.event.processor;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.Liver;

public abstract class Processor<T extends Liver> extends EventChecker<T>{

	@Override
	protected Event popEvent(T target, long eventTime) {
		process(target, eventTime);
		return null;
	}
	
	public abstract void process(T target, long processTime);

}
