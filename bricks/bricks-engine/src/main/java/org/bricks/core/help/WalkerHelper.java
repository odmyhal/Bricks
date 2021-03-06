package org.bricks.core.help;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.neve.WalkLastPrint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Walker;

public class WalkerHelper {

	public static void generalLastPointVector(WalkLastPrint<? extends Walker<?, Fpoint>, Fpoint> walker, Point touchPoint, Fpoint dst){
		Fpoint origin = walker.getOrigin().source;
		
		float rotationSpeed = 0;
		if(walker.getRotation() != 0){
			rotationSpeed = walker.lastRotation();
		}
		Fpoint vector;
		if(walker.getVector().isZero()){
			vector = walker.getVector().source;
		}else{
			vector = walker.lastMove();
		}
		if(rotationSpeed == 0){
			dst.set(vector.x, vector.y);
			return;
		}
		
		dst.setX(touchPoint.getFX() - origin.x);
		dst.setY(touchPoint.getFY() - origin.y);
		
		PointHelper.rotatePointByZero(dst, rotationSpeed > 0 ? 1d : -1d, 0d, dst);
		
		double len = VectorHelper.vectorLen(dst) * rotationSpeed;
		PointHelper.normalize(dst, len);
		
		dst.x += vector.x;
		dst.y += vector.y;
	}
/*	
	public static void generalPointVector(WalkPrint<?, Fpoint> walker, Point touchPoint, Fpoint dst){
		Fpoint origin = walker.getOrigin().source;
		
		float rotationSpeed = walker.getRotationSpeed();
		Fpoint vector = walker.getVector().source;
		
		if(rotationSpeed == 0){
			dst.set(vector.x, vector.y);
			return;
		}
		
		dst.setX(touchPoint.getFX() - origin.x);
		dst.setY(touchPoint.getFY() - origin.y);
		
		PointHelper.rotatePointByZero(dst, rotationSpeed > 0 ? 1d : -1d, 0d, dst);
		
		double len = VectorHelper.vectorLen(dst) * rotationSpeed;
		PointHelper.normalize(dst, len);
		
		dst.x += vector.x;
		dst.y += vector.y;
	}*/
}
