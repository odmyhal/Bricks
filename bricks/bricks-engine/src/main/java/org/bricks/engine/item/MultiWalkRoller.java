package org.bricks.engine.item;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Origin2D;
import org.bricks.engine.tool.Walk;
import org.bricks.engine.tool.Walk2D;

public abstract class MultiWalkRoller<S extends Subject, P extends WalkPrint> extends MultiWalker<S, P, Fpoint> {
/*
	protected MultiWalkRoller() {
	}
*/	
	@Override
	public void applyRotation(){
		super.applyRotation();
		rollMoveVector();
	}
	
	private void rollMoveVector(){
		float rad = lastRotation();
		Origin<Fpoint> vector = getVector();
		double sin = Math.sin(rad), cos = Math.cos(rad);
		PointHelper.rotatePointByZero(vector.source, sin, cos, vector.source);
		if(!this.acceleration.isZero()){
			PointHelper.rotatePointByZero(acceleration.source, sin, cos, acceleration.source);
		}
	}
	
	protected Walk<Fpoint> provideInitialLegs(){
		return new Walk2D(this);
	}
	
	public Origin<Fpoint> provideInitialOrigin(){
		return new Origin2D();
	}
}
