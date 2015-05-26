package org.bricks.core.help;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;

public class VectorHelper {

	
	public static float scalarMult(Point one, Point two){
		return one.getFX() * two.getFX() + one.getFY() * two.getFY();
	}
	
	public static double vectorLen(Point v){
//		return Math.hypot(v.getFX(), v.getFY());
		return Math.sqrt(v.getFX() * v.getFX() + v.getFY() * v.getFY());
	}
	
	public static double vectorProjectionModule(Point target, Point base){
		return scalarMult(target, base) / vectorLen(base);
	}
	
	public static Fpoint vectorProjection(Point target, Point base){
		Fpoint res = new Fpoint(base.getFX(), base.getFY());
		double len = vectorProjectionModule(target, base);
		return PointHelper.normalize(res, len);
	}
}
