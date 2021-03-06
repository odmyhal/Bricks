package org.bricks.core.help;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.Fpoint;
import org.bricks.engine.help.GausHelper;

public class AlgebraHelper {
	
	public static float triangleArea(Point one, Point two, Point three){
		return (float) Math.abs((one.getFX()*(two.getFY() - three.getFY()) + 
				two.getFX()*(three.getFY() - one.getFY()) + 
				three.getFX() * (one.getFY() - two.getFY())) / 2);
		
	}
	
	/**
	 * 
	 * Not used yet because of case when both lines are equal
	 * @return
	 */
	public static Point pointCrossGaus(float A1X, float A1Y, float A2X, float A2Y
			, float B1X, float B1Y, float B2X, float B2Y){
		float a = GausHelper.resolveFirstOf4(A2X - A1X, 0f, -1f, 0f, A1X, 
				A2Y - A1Y, 0f, 0f, -1f, A1Y, 
				0f, B2X - B1X, -1f, 0f, B1X, 
				0f, B2Y - B1Y, 0f, -1f, B1Y);
		if(a == Float.NEGATIVE_INFINITY){
			return null;
		}else{
			return new Fpoint(A1X + (A2X - A1X) * a, A1Y + (A2Y - A1Y) * a);
		}
		
/*		float RAX = A1X + (A2X - A1X) * a;
		float RAY = A1Y + (A2Y - A1Y) * a;
		
		float RBX = B1X + (B2X - B1X) * b;
		float RBY = B1Y + (B2Y - B1Y) * b;
		
		//A1X + (A2X - A1X) * a = B1X + (B2X - B1X) * b
		a = (B1X - A1X + (B2X - B1X) * b) / A1X;
		Fpoint res = new Fpoint();
		return res;*/
	}
	
	public static Point pointCross(float A1X, float A1Y, float A2X, float A2Y
			, float B1X, float B1Y, float B2X, float B2Y){
		float a, b, a1=0, b1=0, a2=0, b2=0;
		
		if(A2X == A1X && B2X == B1X){
			if(A1X == B1X){
				float aMinY = Math.min(A1Y, A2Y);
				float aMaxY = Math.max(A1Y, A2Y);
				float bMinY = Math.min(B1Y, B2Y);
				float bMaxY = Math.max(B1Y, B2Y);
				if( aMaxY < bMinY || bMaxY < aMinY){
					return null;
				}else{
					float y = (Math.min(aMaxY, bMaxY) + Math.max(aMinY, bMinY)) / 2;
					return new Fpoint(A1X, y);
				}
			}
			return null;
		}else if(A2X == A1X){
			a = A1X;
			a2 = (B2Y - B1Y) / (B2X - B1X);
			b2 = B1Y - a2 * B1X;
			b = a2 * a + b2;
			if(b < Math.min(A1Y, A2Y) || b > Math.max(A1Y, A2Y) ){
				return null;
			}
			if(b < Math.min(B1Y, B2Y) || b > Math.max(B1Y, B2Y) ){
				return null;
			}
		}else if(B2X == B1X){
			a = B1X;
			a1 = (A2Y - A1Y) / (A2X - A1X);
			b1 = A1Y - a1 * A1X;
			b = a1 * a + b1;
			if(b < Math.min(A1Y, A2Y) || b > Math.max(A1Y, A2Y) ){
				return null;
			}
			if(b < Math.min(B1Y, B2Y) || b > Math.max(B1Y, B2Y) ){
				return null;
			}
		}else{
			a1 = (A2Y - A1Y) / (A2X - A1X);
			a2 = (B2Y - B1Y) / (B2X - B1X);
			b1 = A1Y - a1 * A1X;
			b2 = B1Y - a2 * B1X;
			if(a1 == a2){
				if(b1 == b2){
					float aMinX = Math.min(A1X, A2X);
					float aMaxX = Math.max(A1X, A2X);
					float bMinX = Math.min(B1X, B2X);
					float bMaxX = Math.max(B1X, B2X);
					if( aMaxX < bMinX || bMaxX < aMinX){
						return null;
					}else{
						float x = (Math.min(aMaxX, bMaxX) + Math.max(aMinX, bMinX)) / 2;
						float y = a1 * x + b1;
						return new Fpoint(x, y);
					}
				}
				return null;
			}
			a = (b1 - b2) / (a2 - a1);
			b = a1 * a + b1;
			if(a < Math.min(A1X, A2X) || a > Math.max(A1X, A2X) ){
				return null;
			}
			if(a < Math.min(B1X, B2X) || a > Math.max(B1X, B2X) ){
				return null;
			}
		}
		return new Fpoint(a, b);
	}
	
	/**
	 * the returned angle is in the range [0; 2*pi).
	 * @param cos
	 * @param sin
	 * @return
	 */
	public static double trigToRadians(double cos, double sin){
		if(cos >= 0){
			if(sin >= 0){
				return Math.asin(sin);
			}else{
				return Math.PI * 2 + Math.asin(sin);
			}
		}else{
			if(sin >= 0){
				return Math.acos(cos);
			}else{
				return Math.PI * 2 - Math.acos(cos);
			}
		}
	}
	
	public static float pow(float x, int p){
		float res = x;
		while(--p > 0){
			res *= x;
		}
		return res;
	}
	
	public static double pow(double x, int p){
		double res = x;
		while(--p > 0){
			res *= x;
		}
		return res;
	}
}
