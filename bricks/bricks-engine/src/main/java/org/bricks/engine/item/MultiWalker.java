package org.bricks.engine.item;

import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.tool.Walk;

public abstract class MultiWalker<S extends Subject<?, ?, C, R>, P extends WalkPrint, C, R extends Roll> extends MultiRoller<S, P, C, R> implements Walker<P, C>{

	private Walk<C> legs;
	private Origin<C> vector;
//	private float acceleration;
	protected Origin<C> acceleration;
	private Origin<C> tmpAcceleration;
	private long accelerationTime;
/*
	protected MultiWalker() {
		legs = new Walk(this);
		vector = new Fpoint(0f, 0f);
	}
*/	
	@Override
	protected void init(){
		legs = provideInitialLegs();
		vector = this.provideInitialOrigin();
		acceleration = this.provideInitialOrigin();
		tmpAcceleration = this.provideInitialOrigin();
		super.init();
	}
	
	protected abstract Walk<C> provideInitialLegs();
	
	@Override
	public void motorProcess(long currentTime){
		processEvents(currentTime);
		if(!alive()){
			return;
		}
		boolean rotate = rotate(currentTime);
		if(rotate){
			applyRotation();
		}
		applyAcceleration(currentTime);
		boolean move = legs.move(currentTime, vector.source);
		if(rotate || move){
			adjustInMotorPrint();
		}
	}
	
	@Override
	protected void adjustInMotorPrint(){
		adjustCurrentPrint(false);
		for(Satellite satellite : getSatellites()){
			satellite.update();
		}
	}
	
	private void applyAcceleration(long curTime){
		if(acceleration.isZero()){
			return;
		}
		float diff = (curTime - accelerationTime) / 1000f;
		tmpAcceleration.set(acceleration);
		tmpAcceleration.mult(diff);
		vector.add(tmpAcceleration);
		accelerationTime = curTime;
/*		if(acceleration != 0){
			float diff = (curTime - accelerationTime) / 1000f;
			Validate.isTrue(diff >= 0, "Need to check accelaration time...");
			if(diff < 0.1){
				return;
			}
			double rotation = getRotation();
			float absAcc = acceleration * diff;
			vector.setX(vector.getFX() + absAcc * (float) Math.cos(rotation));
			vector.setY(vector.getFY() + absAcc * (float) Math.sin(rotation));
			Validate.isTrue(vector.getFX() < 3000, "Speed X is to hie");
			Validate.isTrue(vector.getFY() < 3000, "Speed Y is to hie");
			accelerationTime = curTime;
		}*/
	}
	
	@Override
	public void flushTimer(long nTime){
		super.flushTimer(nTime);
		legs.flushTimer(nTime);
//		System.out.println("CHECK: " + this.getClass().getCanonicalName() + " has flushed timer!!!");
	}

	public void setVector(Origin<C> v) {
		this.vector.set(v);
	}
	
	public Origin<C> getVector() {
		return vector;
	}
	

	public void setAcceleration(Origin<C> acc, long accTime){
		this.acceleration.set(acc);
		accelerationTime = accTime;
	}
	
	public Origin<C> getAcceleration(){
		return this.acceleration;
	}
	

	@Override
	public void rollBack(long currentTime){
		boolean moveBack = legs.moveBack(currentTime);
		boolean rotateBack = rotateBack(currentTime);
		if(rotateBack){
			applyRotation();
		}
		if(rotateBack || moveBack){
			adjustCurrentPrint(false);
			for(Satellite satellite : getSatellites()){
				satellite.update();
			}
/*			for(S subject : getStaff()){
				SectorMonitor.monitor(subject);
				subject.adjustCurrentView();
			}*/
		}
	}
	
//	@Override
	public void translateNoView(Origin<C> origin){
		this.translate(origin, false);
	}
	
	@Override
	public P print(){
		return (P) new WalkPrint(this.printStore);
	}
/*	
	@Override
	public WalkView provideCurrentView(){
		return new WalkView(this);
	}
	*/
}
