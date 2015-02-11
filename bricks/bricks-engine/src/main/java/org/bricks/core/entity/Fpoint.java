package org.bricks.core.entity;

public class Fpoint implements Point{

private float x, y;
	
	public Fpoint(float x, float y){
		this.x = x;
		this.y = y;
	}

	public float getFX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getFY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return (int) Math.round(x);
	}
	
	public int getY(){
		return (int) Math.round(y);
	}
	
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public String toString(){
		return String.format("Point(x: %.5f ,  y: %.5f)", x, y);
	}
}
