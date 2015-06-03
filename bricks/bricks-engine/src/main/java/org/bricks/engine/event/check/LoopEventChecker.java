package org.bricks.engine.event.check;

import org.bricks.engine.staff.Liver;

public class LoopEventChecker<T extends Liver<?>> extends ChunkEventChecker<T> {

	public LoopEventChecker(CheckerType checkerType, EventChecker<T>... chrs) {
		super(checkerType, chrs);
	}
	
	protected boolean indexBack(){
		if(!super.indexBack()){
			nextIndex = this.size() - 1;
		}
		return true;
	}

	protected void finish(T target, long eventTime){
		reject();
	}
}
