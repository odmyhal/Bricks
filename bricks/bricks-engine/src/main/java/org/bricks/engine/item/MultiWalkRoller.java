package org.bricks.engine.item;

import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Roll;

public abstract class MultiWalkRoller<S extends Subject<?, ?, C, R>, P extends WalkPrint, C, R extends Roll> 
	extends MultiWalker<S, P, C, R> {
/*
	protected MultiWalkRoller() {
	}
*/	
	@Override
	public void applyRotation(){
		super.applyRotation();
		rollMoveVector();
	}
	
	protected abstract void rollMoveVector();
	
}
