package org.bricks.core.entity;

public class Ipoint implements Point{
	
	private int x, y;
	
	public Ipoint(int x, int y){
		this.x = x;
		this.y = y;
	}

	public float getFX() {
		return x;
	}

	public float getFY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	public void translate(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public String toString(){
		return String.format("Point(x: %d ,  y: %d)", x, y);
	}
}
