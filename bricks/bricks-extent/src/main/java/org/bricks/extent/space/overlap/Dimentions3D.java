package org.bricks.extent.space.overlap;

import com.badlogic.gdx.math.Vector3;

public class Dimentions3D {

	public final Vector3 max = new Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
	public final Vector3 min = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
	
	public void apply(Vector3 point){
		if(point.x > max.x){
			max.x = point.x;
		}else if(point.x < min.x){
			min.x = point.x;
		}
		if(point.y > max.y){
			max.y = point.y;
		}else if(point.y < min.y){
			min.y = point.y;
		}
		if(point.z > max.z){
			max.z = point.z;
		}else if(point.z < min.z){
			min.z = point.z;
		}
	}
	
	public void reject(){
		max.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		min.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
	}
	
}
