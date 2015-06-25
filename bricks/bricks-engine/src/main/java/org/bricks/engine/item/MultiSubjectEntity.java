package org.bricks.engine.item;

import java.util.ArrayList;
import java.util.List;

import org.bricks.exception.Validate;
import org.bricks.engine.Engine;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Origin;

public abstract class MultiSubjectEntity<S extends Subject, P extends EntityPrint, C> extends PrintableBase<P> implements Entity<P>{

	protected ArrayList<S> staff = new ArrayList<S>();
	protected ArrayList<Satellite> satellites = new ArrayList<Satellite>();
	private Origin<C> origin;
	private Engine engine;
	
	protected MultiSubjectEntity(){
		origin = provideInitialOrigin();
		init();
	}
	
	public abstract Origin<C> provideInitialOrigin();
	
	protected void init(){
		initPrintStore();
	}
	
	public void applyEngine(Engine engine){
//		Validate.isTrue(this.engine == null);
		this.engine = engine;
	}
	
	public Engine getEngine(){
		return this.engine;
	}
	
	public void disappear(){
		for(int i = 0; i < staff.size(); i++){
			staff.get(i).leaveDistrict();
		}
	}
	
	public void outOfWorld(){
		disappear();
	}
	
	public boolean addSubject(S subject){
		if(staff.add(subject)){
			subject.setEntity(this);
			subject.translate(origin);
			addSatellite(subject);
			return true;
		}
		return false;
	}
	
	public boolean addSatellite(Satellite satellite){
		return satellites.add(satellite);
	}
	
	public void translate(Origin trn){
		this.translate(trn, true);
	}

	protected void translate(Origin<C> trn, boolean adjustView) {
		this.origin.add(trn);
		for(int i = 0; i < satellites.size(); i++){
			satellites.get(i).translate(trn);
		}
		if(adjustView){
			this.adjustCurrentPrint();
		}
	}

	public Origin<C> origin(){
		return this.origin;
	}
	
	public ArrayList<S> getStaff(){
		return staff;
	}
	
	public ArrayList<Satellite> getSatellites(){
		return satellites;
	}

	protected final int adjustCurrentPrint(boolean adjustSubjects){
		int r = printStore.adjustCurrentPrint();
		if(adjustSubjects){
			adjustSubjectsViews();
		}
		return r;
	}
	
	public int adjustCurrentPrint(){
		return adjustCurrentPrint(true);
	}
	
	public P print(){
		return (P) new EntityPrint(printStore);
	}
	
	protected void adjustSubjectsViews(){
		for(int i = 0; i < staff.size(); i++){
			staff.get(i).adjustCurrentPrint();
		}
	}

}
