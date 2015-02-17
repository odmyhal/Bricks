package org.bricks.engine.view;

import java.util.LinkedList;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.staff.Entity;

@Deprecated
public class EntityView<E extends Entity> extends DurableView{

	public EntityView(LinkedList backet) {
		super(backet);
		// TODO Auto-generated constructor stub
	}

	private final Ipoint origin  = new Ipoint(0, 0);
	protected E entity;
/*	
	public EntityView(E entity){
		super(entity.getViewCache());
		this.entity = entity;
//		this.init();
	}
*/	
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
