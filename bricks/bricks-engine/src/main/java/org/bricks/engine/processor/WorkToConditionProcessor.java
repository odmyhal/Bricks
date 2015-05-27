package org.bricks.engine.processor;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.processor.Processor;
import org.bricks.engine.staff.Liver;

public abstract class WorkToConditionProcessor<T extends Liver> extends Processor<T> {

	
	public WorkToConditionProcessor(CheckerType type) {
		super(type);
	}

	public void process(T target, long processTime){
		doJob(target, processTime);
		if(stopCondition(target, processTime)){
			disable();
			target.unregisterEventChecker(this);
		}
	}
	
	public abstract void doJob(T target, long processTime);
	
	public abstract boolean stopCondition(T target, long processTime); 
}
