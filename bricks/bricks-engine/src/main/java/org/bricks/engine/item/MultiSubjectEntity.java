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
import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;

public abstract class MultiSubjectEntity<S extends Subject, P extends EntityPrint> extends PrintableBase<P> implements Entity<P>{

	private List<S> staff = new ArrayList<S>();
	private List<Satellite> satellites = new ArrayList<Satellite>();
	private Ipoint orign = new Ipoint(0, 0);
//	protected Ipoint constructOrigin;
	private float weight = 0;
	private Engine engine;
	
//	protected PrintStore<? extends Entity, P> printStore;
	
	protected MultiSubjectEntity(){
		init();
	}
	
	protected void init(){
//		adjustCurrentView();
//		this.printStore = new PrintStore(this);
		initPrintStore();
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
//		this.adjustSubjectsViews();
		this.adjustCurrentPrint();
/*		for(S subject : staff){
			subject.adjustCurrentPrint();
		}*/
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
//			this.adjustSubjectsViews();
			this.adjustCurrentPrint();
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

	protected final int adjustCurrentPrint(boolean adjustSubjects){
//		System.out.println("MultiSubject entity adjust current print...");
		int r = printStore.adjustCurrentPrint();
		if(adjustSubjects){
			adjustSubjectsViews();
		}
		return r;
	}
	
	public final int adjustCurrentPrint(){
		return adjustCurrentPrint(true);
	}
	
	public P print(){
		return (P) new EntityPrint(printStore);
	}
	
	protected void adjustSubjectsViews(){
		for(Subject subject : getStaff()){
			subject.adjustCurrentPrint();
		}
	}

}
