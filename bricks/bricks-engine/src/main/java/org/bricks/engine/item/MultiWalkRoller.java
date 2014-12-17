package org.bricks.engine.item;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.pool.Subject;

public abstract class MultiWalkRoller<S extends Subject> extends MultiWalker<S> {

	protected MultiWalkRoller() {
	}
	
	@Override
	public void applyRotation(){
		super.applyRotation();
		rollMoveVector();
	}
	
	private void rollMoveVector(){
		float rad = lastRotation();
		Fpoint vector = getVector();
		PointHelper.rotatePointByZero(vector, Math.sin(rad), Math.cos(rad), vector);
	}
}
