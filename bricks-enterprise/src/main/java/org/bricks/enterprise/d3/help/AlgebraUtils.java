package org.bricks.enterprise.d3.help;

public class AlgebraUtils {

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
}
