package org.bricks.core.entity;


public class Dimentions{
	
	private Point minX, minY, maxX, maxY;
	
	private static final Point initMinX = new Fpoint(Float.MAX_VALUE, 0);
	private static final Point initMaxX = new Fpoint(Float.NEGATIVE_INFINITY, 0);
	private static final Point initMinY = new Fpoint(0, Float.MAX_VALUE);
	private static final Point initMaxY = new Fpoint(0, Float.NEGATIVE_INFINITY);
	
	public Dimentions(){
		this.reject();
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
	
	public void applyPoint(Point point){
		if(getMinX() > point.getFX()){
			setMinXPoint(point);
		}
		if(getMaxX() < point.getFX()){
			setMaxXPoint(point);
		}
		if(getMinY() > point.getFY()){
			setMinYPoint(point);
		}
		if(getMaxY() < point.getFY()){
			setMaxYPoint(point);
		}
	}
	
	public void reject(){
		minX = initMinX;
		maxX = initMaxX;
		minY = initMinY;
		maxY = initMaxY;
	}
	
	public String toString(){
		return String.format("Dimentions. MinX: %.2f, MaxX: %.2f, MinY: %.2f,  MaxY: %.2f", getMinX(), getMaxX(), getMinY(), getMaxY());
	}
	
}
