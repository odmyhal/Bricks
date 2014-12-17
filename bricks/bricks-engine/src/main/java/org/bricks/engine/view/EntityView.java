package org.bricks.engine.view;

import java.util.LinkedList;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.staff.Entity;

public class EntityView<E extends Entity> extends DurableView{

	private final Ipoint origin  = new Ipoint(0, 0);
	protected E entity;
	
	public EntityView(E entity){
		super(entity.getViewCache());
		this.entity = entity;
//		this.init();
	}
	
	public Ipoint getOrigin(){
		return origin;
	}
	
	public void init(){
		Ipoint eOrigin = entity.getOrigin();
		this.origin.setX(eOrigin.getX());
		this.origin.setY(eOrigin.getY());
	}
	
	protected E getEntity(){
		return entity;
	}
/*	
	protected void initByEntity(){
		Ipoint eOrigin = entity.getOrigin();
		this.origin.setX(eOrigin.getX());
		this.origin.setY(eOrigin.getY());
	}*/
}
