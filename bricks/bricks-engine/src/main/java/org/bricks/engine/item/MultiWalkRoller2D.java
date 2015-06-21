package org.bricks.engine.item;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Origin2D;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.tool.Walk;
import org.bricks.engine.tool.Walk2D;

public abstract class MultiWalkRoller2D<S extends Subject<?, ?, Fpoint, Roll>, P extends WalkPrint>
	extends MultiWalkRoller<S, P, Fpoint, Roll>{


	protected void rollMoveVector(){
		float rad = lastRotation();
		Origin<Fpoint> vector = getVector();
		double sin = Math.sin(rad), cos = Math.cos(rad);
		PointHelper.rotatePointByZero(vector.source, sin, cos, vector.source);
		if(!this.acceleration.isZero()){
			PointHelper.rotatePointByZero(acceleration.source, sin, cos, acceleration.source);
		}
	}
	
	protected Roll initializeRoll(){
		return new Roll();
	}
	
	protected Walk<Fpoint> provideInitialLegs(){
		return new Walk2D();
	}
	
	public Origin<Fpoint> provideInitialOrigin(){
		return new Origin2D();
	}
}
