package org.bricks.engine.processor;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.Liver;

public abstract class Processor<T extends Liver> extends EventChecker<T>{

	public Processor(CheckerType type) {
		super(type);
	}

	@Override
	protected Event popEvent(T target, long eventTime) {
		process(target, eventTime);
		return null;
	}
	
	public abstract void process(T target, long processTime);

}
