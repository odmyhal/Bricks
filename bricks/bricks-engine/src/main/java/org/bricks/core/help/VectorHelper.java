package org.bricks.core.help;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.exception.Validate;

public class VectorHelper {

	
	public static float scalarMult(Point one, Point two){
		return one.getFX() * two.getFX() + one.getFY() * two.getFY();
	}
	
	public static double vectorLen(Point v){
//		return Math.hypot(v.getFX(), v.getFY());
		return Math.sqrt(v.getFX() * v.getFX() + v.getFY() * v.getFY());
	}
	
	public static double vectorProjectionModule(Point target, Point base){
		double vectorLen = vectorLen(base);
		Validate.isTrue(vectorLen > 0, "Point base can't be zero vector, " + base + " given...");
		return scalarMult(target, base) / vectorLen;
	}
	
	public static Fpoint vectorProjection(Point target, Point base, Fpoint dest){
		dest.set(base.getFX(), base.getFY());
		double len = vectorProjectionModule(target, base);
		return PointHelper.normalize(dest, len);
	}
	
	public static boolean hasSameDirection(Point one, Point two){
		return one.getFX() * two.getFX() >= 0 && one.getFY() * two.getFY() >= 0;
	}
}
