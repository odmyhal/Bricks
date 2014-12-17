package org.bricks.core.help;

import java.util.Comparator;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.Fpoint;

public class PointHelper {
	
	private static Comparator<Point> pointXComparator = new PointXComparator();
	private static Comparator<Point> pointYComparator = new PointYComparator();
	public final static Point zeroPoint = new Fpoint(0, 0);

	public static Comparator<Point> pointXComparator() {
		return pointXComparator;
	}

	public static Comparator<Point> pointYComparator() {
		return pointYComparator;
	}
	
	public static float calcYOnLine(Point one, Point two, float middleX){
		return (two.getFY() - one.getFY()) * (middleX - one.getFX()) / (two.getFX() - one.getFX()) + one.getFY();
	}
	
	public static float calcXOnLine(Point one, Point two, float middleY){
		return (two.getFX() - one.getFX()) * (middleY - one.getFY()) / (two.getFY() - one.getFY()) + one.getFX();
	}
	
	private static class PointXComparator implements Comparator<Point>{

		public int compare(Point arg0, Point arg1) {
			float xDiff = ((Point) arg0).getFX() - ((Point) arg1).getFX();
			if(xDiff > 0){
				return 1;
			}
			if(xDiff < 0){
				return -1;
			}
			return 0;
		}
		
	}
	
	private static class PointYComparator implements Comparator<Point>{

		public int compare(Point arg0, Point arg1) {
			float yDiff = ((Point) arg0).getFY() - ((Point) arg1).getFY();
			if(yDiff > 0){
				return 1;
			}
			if(yDiff < 0){
				return -1;
			}
			return 0;
		}
		
	}
	
	public static int detectSectorByPoint(Point centralP, Point targetP){
		float translateX = targetP.getFX() - centralP.getFX();
		float translateY = targetP.getFY() - centralP.getFY();
		int result = 0;
		if(translateX >= 0 && translateY >= 0){
			result = 1;
		}else if(translateX < 0 && translateY >= 0){
			result = 2;
		}else if(translateX < 0 && translateY < 0){
			result = 3;
		}else if(translateX >= 0 && translateY < 0){
			result = 4;
		}
		Validate.isTrue(result > 0, "Somethinc wrong with central & target point. Could not detect sector");
		return result;
	}
	
	public static int detectSectorByZero(Point p){
		return detectSectorByPoint(zeroPoint, p);
	}


	public static Point normalize(Fpoint p){
		return normalize(p, 1f);
	}
	
	public static Fpoint normalize(Fpoint p, double len){
		Validate.isTrue(p.getFX() != 0f || p.getFY() != 0f, "Could not normalize zero point");
		if(len == 0){
			p.setX(0);
			p.setY(0);
			return p;
		}
		double k = Math.sqrt(p.getFX() * p.getFX() + p.getFY() * p.getFY()) / len;
		if(Math.abs(k - 1) > 0.0000001){
			p.setX((float) (p.getFX() / k));
			p.setY((float) (p.getFY() / k));
		}
		return p;
	}
/*	
	public static Ipoint rotatePointByCenter(Point p, Point c, double sin, double cos){
		float nx = p.getFX() - c.getFX();
		float ny = p.getFY() - c.getFY();
		double x = nx * cos - ny * sin + c.getFX();
		double y = nx * sin + ny * cos + c.getFY();
		return new Ipoint((int) Math.round(x), (int) Math.round(y));
	}
*/	

	public static Ipoint rotatePointByZero(Point p, double sin, double cos, Ipoint target){
		double x = p.getFX() * cos - p.getFY() * sin;
		double y = p.getFX() * sin + p.getFY() * cos;
		target.setX((int) Math.round(x));
		target.setY((int) Math.round(y));
		return target;
	}
	
	public static Fpoint rotatePointByZero(Point p, double sin, double cos, Fpoint target){
		double x = p.getFX() * cos - p.getFY() * sin;
		double y = p.getFX() * sin + p.getFY() * cos;
		target.setX((float)x);
		target.setY((float)y);
		return target;
	}
	
	public static Point pointCross(Point a1, Point a2, Point b1, Point b2){
		return AlgebraHelper.pointCross(a1.getFX(), a1.getFY(), a2.getFX(), a2.getFY(), b1.getFX(), b1.getFY(), b2.getFX(), b2.getFY());
	}
}
