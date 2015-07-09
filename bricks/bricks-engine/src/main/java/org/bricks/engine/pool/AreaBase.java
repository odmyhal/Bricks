package org.bricks.engine.pool;

import java.util.ArrayList;
import java.util.Iterator;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.impl.BrickWrap;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Subject;

public abstract class AreaBase<E extends EntityCore> extends BrickWrap<PointSetPrint> implements Pool{

	private Ipoint corner;
	private Ipoint dimm;
	protected PoolSlot[] pool;
	
	private AreaBase(Ipoint corner, int capacity, int xLen, int yLen){
		super(createBrick(corner, xLen, yLen));
		this.corner = corner;
		dimm = new Ipoint(xLen, yLen);
		this.pool = new PoolSlot[capacity];
		for(int i = 0; i < capacity; i++){
			this.pool[i] = new PoolSlot();
		}
		this.adjustCurrentPrint();
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
	
	protected Habitant<E> freeSubject(int i){
		return pool[i].freeSubject();
	}
	
	protected int addSubject(Habitant<E> subject){
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
	
	public Habitant getSubject(int num){
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
	
	private static PointSetBrick createBrick(Ipoint corner, int xLen, int yLen){
		ArrayList<Ipoint> myPoints = new ArrayList(4);
		myPoints.add(corner);
		myPoints.add(new Ipoint(corner.getX() + xLen - 1, corner.getY()));
		myPoints.add(new Ipoint(corner.getX() + xLen - 1, corner.getY() + yLen - 1));
		myPoints.add(new Ipoint(corner.getX(), corner.getY() + yLen -1));
//		Ipoint center = new Ipoint(corner.getX() + (int) Math.round(xLen / 2), corner.getY() + (int) Math.round(yLen / 2));
		PointSetBrick psb = new PointSetBrick(myPoints);
		Point bCenter = psb.getCenter();
		return psb;
//		this.view = new PointSetView(myPoints, center);
	}

	public PointSetPrint getPrint(){
		return getInnerPrint();
	}
	
	public boolean containsPoint(Point p){
		return p.getFX() >= corner.getFX() && p.getFY() >= corner.getFY()
				&& p.getFX() < corner.getFX() + dimm.getFX() 
				&& p.getFY() < corner.getFY() + dimm.getFY();
	}
	
	public boolean intersectLine(Point begin, Point end){
		if(containsPoint(begin) || containsPoint(end)){
			return true;
		}
		Point one = null, two = null;
		Iterator<Ipoint> iterator = this.brick.getPoints().iterator();
		while(iterator.hasNext()){
			if(one == null){
				one = iterator.next();
//				first = one;
			}
			two = iterator.next();
			if(PointHelper.pointCross(one, two, begin, end) != null){
				return true;
			}
			one = two;
		}
		return false;
//no need to check last line
//		return PointHelper.pointCross(one, first, begin, end) != null;
	}
}
