package org.bricks.engine.item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Ipoint;
import org.bricks.engine.Engine;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.view.EntityView;

public abstract class MultiSubjectEntity<S extends Subject, P extends EntityPrint> implements Entity<P>{

	private List<S> staff = new ArrayList<S>();
	private List<Satellite> satellites = new ArrayList<Satellite>();
	private Ipoint orign = new Ipoint(0, 0);
//	protected Ipoint constructOrigin;
	private float weight = 0;
	private Engine engine;
	
	protected PrintStore<? extends Entity, P> printStore;
	
	protected MultiSubjectEntity(){
		init();
	}
	
	protected void init(){
//		adjustCurrentView();
		this.printStore = new PrintStore(this);
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
			subject.adjustCurrentPrint();
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
	
	public void translate(int x, int y){
		this.translate(x, y, true);
	}

	protected void translate(int x, int y, boolean adjustView) {
		orign.translate(x, y);
		for(Satellite satellite : getSatellites()){
			satellite.translate(x, y);
		}
		if(adjustView){
			this.adjustCurrentPrint();
			this.adjustSubjectsViews();
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
/*	
	public void adjustCurrentView(){
		adjustCurrentView(true); 
	}

	public LinkedList<EntityView> getViewCache(){
		return this.viewCache;
	}*/
/*	
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
	*/
	protected final void adjustCurrentPrint(boolean adjustSubjects){
		printStore.adjustCurrentPrint();
		if(adjustSubjects){
			adjustSubjectsViews();
		}
	}
	
	public final void adjustCurrentPrint(){
		adjustCurrentPrint(true);
	}
	
	public P getSafePrint(){
		return printStore.getSafePrint();
	}
	
	public P getInnerPrint(){
		return printStore.getInnerPrint();
	}
	
	public P print(){
		return (P) new EntityPrint(printStore);
	}
	
	protected void adjustSubjectsViews(){
		for(Subject subject : getStaff()){
			subject.adjustCurrentPrint();
		}
	}
/*	
	public EntityView getCurrentView(){
		synchronized(viewCache){
			this.view.occupy();
			return this.view;
		}
	}
	*/
}
