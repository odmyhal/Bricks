package org.bricks.engine.event.processor;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.staff.Liver;

public abstract class SingleActProcessor<T extends Liver> extends Processor<T> {

	public SingleActProcessor(CheckerType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public final void process(T target, long processTime) {
		if(/*isActive() && */ready(target, processTime)){
			disable();
			target.unregisterEventChecker(this);
			processSingle(target, processTime);
		}
	}

	protected abstract boolean ready(T target, long processTime);
	
	protected abstract void processSingle(T target, long processTime);
	
}
