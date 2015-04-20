package org.bricks.engine.help;

public class GausHelper {

	public static final float resolveFirstOf3(float a1, float b1, float c1, float d1,
										float a2, float b2, float c2, float d2,
										float a3, float b3, float c3, float d3){

		if(c3 == 0){
			if(c2 == 0){
				return resolveFirstOf2(a2, b2, d2, a3, b3, d3);
			}
			float k1 = - c1 / c2;
			a1 += a2 * k1;
			b1 += b2 * k1;
			d1 += d2 * k1;
			return resolveFirstOf2(a1, b1, d1, a3, b3, d3);
		}
		float k1 = - c1 / c3;
		a1 += a3 * k1;
		b1 += b3 * k1;
		d1 += d3 * k1;
		
		float k3 = - c2 / c3;
		a2 += a3 * k3;
		b2 += b3 * k3;
		d2 += d3 * k3;
		
		return resolveFirstOf2(a1, b1, d1, a2, b2, d2);
	}
	
	public static final float resolveFirstOf2(float a1, float b1, float d1,
											float a2, float b2, float d2){

		if(b2 == 0){
			if(a2 == 0){
				if(b1 == 0){
					if(a1 == 0){
						return Float.NEGATIVE_INFINITY;
					}else{
						return -d1 / a1;
					}
				}
				return Float.NEGATIVE_INFINITY;
			}
			return -d2 / a2;
		}
		float k = -b1 / b2;
		a1 += a2 * k;
		d1 += d2 * k;
		if(a1 == 0){
			return Float.NEGATIVE_INFINITY;
		}
		return -d1 / a1;
	}
}
