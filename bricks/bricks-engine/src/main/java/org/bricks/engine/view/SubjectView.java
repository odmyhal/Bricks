package org.bricks.engine.view;

import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;

public class SubjectView<P extends Entity, V extends EntityView> extends PointSetView{

	private Subject<P> s;
	private V entityView;
	
	public SubjectView(Subject<P> s){
		super(s.getViewCache(), s.getBrick().size());
		this.s = s;
		this.init();
	}
	
	public void init(){
		this.init(s.getBrick().getPoints(), s.getCenter());
		this.entityView = (V) s.getEntity().getCurrentView();
	}
	
	public float getWeight(){
		return s.getWeight();
	}
	
	public Subject<P> getSubject(){
		return s;
	}
	
	public V getEntityView(){
		return entityView;
	}
	
	@Override
	protected void endUse(){
		super.endUse();
		entityView.free();
	}

}
