package org.bricks.extent.space;

import org.bricks.core.entity.Point;

import com.badlogic.gdx.math.Vector3;

public class Point3D implements Point{

	private Vector3 vector3;
	
	public Point3D(){
		vector3 = new Vector3();
	}
	
	public Point3D(Vector3 v3){
		vector3 = v3;
	}
	
	public void set(Vector3 v3){
		this.vector3.set(v3);
	}

	public float getFX() {
		return vector3.x;
	}

	public float getFY() {
		return vector3.y;
	}

	public int getX() {
		return (int) vector3.x;
	}

	public int getY() {
		return (int) vector3.y;
	}

	public void translate(float x, float y) {
		throw new RuntimeException("Not allowed to translate Point3D");
	}

	@Override
	public String toString(){
		return vector3.toString();
	}
}
