package org.bricks.engine.pool;

import java.util.ArrayList;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.view.PointSetView;

public abstract class AreaBase<E extends Entity> implements Pool {

	private Ipoint corner;
	private Ipoint dimm;
	private volatile PointSetView view;
	protected PoolSlot[] pool;
	
	private AreaBase(Ipoint corner, int capacity, int xLen, int yLen){
		this.corner = corner;
		this.dimm = new Ipoint(xLen, yLen);
		this.pool = new PoolSlot[capacity];
		for(int i = 0; i < capacity; i++){
			this.pool[i] = new PoolSlot();
		}
		createView();
	}
	
	public AreaBase(Ipoint corner, int capacity, int len){
		this(corner, capacity, len, len);
	}
	
	public boolean coverSubject(Subject subject){
		Point sCenter = subject.getCenter();
		boolean x = corner.getX() <= sCenter.getX() && corner.getX() + dimm.getX() > sCenter.getX();
		boolean y = corner.getY() <= sCenter.getY() && corner.getY() + dimm.getY() > sCenter.getY();
		return x && y;
	}
	
	synchronized Subject<E> freeSubject(int i){
		return pool[i].freeSubject();
	}
	
	int addSubject(Subject<E> subject){
		for(int i=0; i<pool.length; i++){
			if(pool[i].setSubject(subject)){
				return i;
			}
		}
		//TODO: Need handler for Pool is full exception
		throw new RuntimeException("Pool is full, need handler");
//		throw new PoolFullException(this);
	}
	
	public boolean containsSubject(Subject subject){
		return subject.inPool(this);
	}
	
	public Subject getSubject(int num){
		return pool[num].getSubject();
	}
	
	public int capacity(){
		return pool.length;
	}
	
	public Ipoint getDimentions(){
		return this.dimm;
	}
	
	public Ipoint getCorner(){
		return this.corner;
	}
	
	private void createView(){
		ArrayList<Ipoint> myPoints = new ArrayList(4);
		myPoints.add(corner);
		myPoints.add(new Ipoint(corner.getX() + dimm.getX() - 1, corner.getY()));
		myPoints.add(new Ipoint(corner.getX() + dimm.getX() - 1, corner.getY() + dimm.getY() - 1));
		myPoints.add(new Ipoint(corner.getX(), corner.getY() + dimm.getY() -1));
		Ipoint center = new Ipoint(corner.getX() + (int) Math.round(dimm.getX() / 2), corner.getY() + (int) Math.round(dimm.getY() / 2));
		this.view = new PointSetView(myPoints, center);
	}
	
//	@Override
	public PointSetView getView(){
		return view;
	}
}
