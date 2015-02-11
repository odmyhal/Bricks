package org.bricks.engine.item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Ipoint;
import org.bricks.engine.Engine;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.view.EntityView;

public abstract class MultiSubjectEntity<S extends Subject> implements Entity{

	private List<S> staff = new ArrayList<S>();
	private List<Satellite> satellites = new ArrayList<Satellite>();
	private Ipoint orign = new Ipoint(0, 0);
//	protected Ipoint constructOrigin;
	private float weight = 0;
	private Engine engine;

	protected EntityView view;
	protected final LinkedList<EntityView> viewCache = new LinkedList<EntityView>();
	
	protected MultiSubjectEntity(){
//		this.constructOrigin = new Ipoint(origin.getX(), origin.getY());
		init();
	}
	
	protected void init(){
		adjustCurrentView();
	}
	
	public void applyEngine(Engine engine){
		Validate.isTrue(this.engine == null);
		this.engine = engine;
	}
	
	public Engine getEngine(){
		return this.engine;
	}
	
	public void outOfWorld(){
		for(Subject subject : getStaff()){
			subject.leaveDistrict();
		}
	}
	
	public void setToRotation(float radians){
		for(Satellite satellite : getSatellites()){
			satellite.rotate(radians, getOrigin());
		}
		for(S subject : staff){
			subject.adjustCurrentView();
		}
	}
	
	public boolean addSubject(S subject){
		if(staff.add(subject)){
			subject.setEntity(this);
			recalculateWeight();
			subject.translate(orign.getX(), orign.getY());
			addSatellite(subject);
			return true;
		}
		return false;
	}
	
	public boolean addSatellite(Satellite satellite){
		return satellites.add(satellite);
	}
	
	private void recalculateWeight(){
		weight = 0;
		for(Subject subj : staff){
			weight += subj.getWeight();
		}
	}
	
//	@Override
	public void translate(int x, int y){
		this.translate(x, y, true);
	}

	protected void translate(int x, int y, boolean adjustView) {
		orign.translate(x, y);
		for(Satellite satellite : getSatellites()){
			satellite.translate(x, y);
		}
/*		for(Subject subject : getStaff()){
			subject.translate(x, y);
		}*/
		if(adjustView){
			this.adjustCurrentView();
		}
	}
	
	public float getWeight(){
		return weight;
	}
	
	public List<S> getStaff(){
		return staff;
	}
	
	public List<Satellite> getSatellites(){
		return satellites;
	}

	public Ipoint getOrigin(){
		return orign;
	}
	
	public void adjustCurrentView(){
		adjustCurrentView(true); 
	}

	public LinkedList<EntityView> getViewCache(){
		return this.viewCache;
	}
	
//	public static final AtomicLong createdViews = new AtomicLong(0);
//	public static final AtomicLong reusedViews = new AtomicLong(0);
//TODO need refuse of synchronization use Concurrent collection and volatile modificator for this.view	
	protected final void adjustCurrentView(boolean adjustSubjects){
		synchronized(viewCache){
			EntityView nView = viewCache.pollFirst();
			if(nView == null){
				nView = this.provideCurrentView();
			}
			nView.init();
			nView.occupy();
			if(this.view != null){
				this.view.free();
			}
			this.view = nView;
		}

		if(adjustSubjects){
			adjustSubjectsViews();
		}
	}
	
	protected EntityView provideCurrentView(){
		return new EntityView(this);
	}
	
	protected void adjustSubjectsViews(){
		for(Subject subject : getStaff()){
			subject.adjustCurrentView();
		}
	}
	
	public EntityView getCurrentView(){
		synchronized(viewCache){
			this.view.occupy();
			return this.view;
		}
	}
}
