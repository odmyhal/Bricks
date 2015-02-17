package org.bricks.core.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.core.help.PointHelper;
import org.bricks.core.help.PointSetHelper;

public class PointSetBrick implements Brick{
	
	
	private ArrayList<Ipoint> points = new ArrayList<Ipoint>();
	private ArrayList<Ipoint> cache = new ArrayList<Ipoint>();
	private float weight;
	private Ipoint center;
	private Fpoint initialCenter;
	private Dimentions innerDimentions;
	
	public PointSetBrick(Collection<Ipoint> points){
		this.points.addAll(points);
		for(Ipoint p: points){
			cache.add(new Ipoint(p.getX(), p.getY()));
		}
		initialCenter = PointSetHelper.detectCentralPoint(points);
		this.weight = calcWeight();
		innerDimentions = PointSetHelper.fetchDimentions(points);
		center = new Ipoint(initialCenter.getX(), initialCenter.getY());
	}

	private float calcWeight(){
		float rw = 0f;
		Iterator<? extends Point> pIter = points.iterator();
		Point one = null, two = null, first = null;
		while(pIter.hasNext()){
			if(one == null){
				one = pIter.next();
				first = one;
			}else{
				one = two;
			}
			two = pIter.next();
			rw += AlgebraHelper.triangleArea(one, two, initialCenter);
		}
		rw += AlgebraHelper.triangleArea(two, first, initialCenter);
		return rw;
	}
	
	public int size(){
		return points.size();
	}

	public float getWeight() {
		return weight;
	}

	public Point getCenter(){
		return center;
	}
	
	public Dimentions getDimentions() {
		return innerDimentions;
	}
	
	public void rotate(float rad, Point origin){
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		for(int i = 0; i < points.size(); i++){
			Ipoint cPoint = PointHelper.rotatePointByZero(points.get(i), sin, cos, cache.get(i));
			cPoint.translate(origin.getX(), origin.getY());
		}
		PointHelper.rotatePointByZero(initialCenter, sin, cos, center);
		center.translate(origin.getX(), origin.getY());
	}
	
	public void translate(int x, int y){
		Iterator<Ipoint> pIter = cache.iterator();
		while(pIter.hasNext()){
			Ipoint rPoint = pIter.next();
			rPoint.setX(rPoint.getX() + x);
			rPoint.setY(rPoint.getY() + y);
		}
		center.translate(x, y);
	}
	
	public List<Ipoint> getPoints(){
		return cache;
	}
}
