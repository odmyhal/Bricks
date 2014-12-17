package org.bricks.core.entity;


public class Dimentions{
	
	private Point minX, minY, maxX, maxY;
	
	private static final Point initMinX = new Fpoint(Float.MAX_VALUE, 0);
	private static final Point initMaxX = new Fpoint(Float.MIN_VALUE, 0);
	private static final Point initMinY = new Fpoint(0, Float.MAX_VALUE);
	private static final Point initMaxY = new Fpoint(0, Float.MIN_VALUE);
	
	public Dimentions(){
		minX = initMinX;
		maxX = initMaxX;
		minY = initMinY;
		maxY = initMaxY;
	}
	
	public float getMinX() {
		return minX.getFX();
	}

	public float getMinY() {
		return minY.getFY();
	}

	public float getMaxX() {
		return maxX.getFX();
	}

	public float getMaxY() {
		return maxY.getFY();
	}
	
	public Point getMinXPoint(){
		return minX;
	}
	
	public void setMinXPoint(Point p){
		minX = p;
	}
	
	public Point getMaxXPoint(){
		return maxX;
	}
	
	public void setMaxXPoint(Point p){
		maxX = p;
	}
	
	public Point getMinYPoint(){
		return minY;
	}
	
	public void setMinYPoint(Point p){
		minY = p;
	}
	
	public Point getMaxYPoint(){
		return maxY;
	}
	
	public void setMaxYPoint(Point p){
		maxY = p;
	}
	
	public String toString(){
		return String.format("Dimentions. MinX: %.2f, MaxX: %.2f, MinY: %.2f,  MaxY: %.2f", getMinX(), getMaxX(), getMinY(), getMaxY());
	}
	
}
