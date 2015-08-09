package org.bricks.engine.item;

import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Accelerator;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.tool.Walk;
import org.bricks.engine.tool.Walk2D;

public abstract class MultiWalker<S extends Subject<?, ?, C, R>, P extends WalkPrint, C, R extends Roll> extends MultiRoller<S, P, C, R> implements Walker<P, C>{

	private Walk<C> legs;
	private Origin<C> vector;
//	private float acceleration;
	protected Origin<C> acceleration;
	private Accelerator<C> accelerator;
//	private Origin<C> tmpAcceleration;
//	private long accelerationTime;
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
//		tmpAcceleration = this.provideInitialOrigin();
		accelerator = this.provideAcceleration();
		super.init();
	}
	
	protected abstract Walk<C> provideInitialLegs();
	
	protected abstract Accelerator<C> provideAcceleration();

	public Origin<C> lastMove(){
		return legs.lastMove();
	}
	
	protected void innerProcess(long currentTime){
		super.innerProcess(currentTime);
		if(legs.move(currentTime, vector.source)){
			translateNoView(legs.lastMove());
			setUpdate();
		}
		applyAcceleration(currentTime);
	}
/*	
	@Override
	protected void adjustInMotorPrint(){
		adjustCurrentPrint(false);
		for(Satellite satellite : getSatellites()){
			satellite.update();
		}
	}
*/	
	private void applyAcceleration(long curTime){
		if(acceleration.isZero()){
			return;
		}
//		float diff = (curTime - accelerationTime) / 1000f;
//		tmpAcceleration.set(acceleration);
//		tmpAcceleration.mult(diff);
		vector.add(accelerator.transformAcceleration(acceleration, curTime));
//		accelerationTime = curTime;
	}
/*	
	@Override
	public void flushTimer(long nTime){
		super.flushTimer(nTime);
		legs.flushTimer(nTime);
//		System.out.println("CHECK: " + this.getClass().getCanonicalName() + " has flushed timer!!!");
	}
	*/
	public void timerSet(long time){
		super.timerSet(time);
		legs.timerSet(time);
		accelerator.timerSet(time);
	}
	
	public void timerAdd(long time){
		super.timerAdd(time);
		legs.timerAdd(time);
		accelerator.timerAdd(time);
	}

	public void setVector(Origin<C> v) {
		this.vector.set(v);
	}
	
	public Origin<C> getVector() {
		return vector;
	}
	

	public void setAcceleration(Origin<C> acc, long accTime){
		this.acceleration.set(acc);
		accelerator.timerSet(accTime);
//		accelerationTime = accTime;
	}
	
	public Origin<C> getAcceleration(){
		return this.acceleration;
	}
	
	@Override
	public void rollBack(long currentTime){
		this.rollBack(currentTime, 1f);
	}
	

//	@Override
	public final void rollBack(long currentTime, float k){
		if(legs.moveBack(currentTime, k)){
			this.translateNoView(legs.lastMove());
			setUpdate();
		}
		if(rotateBack(currentTime, k)){
			applyRotation();
			setUpdate();
		}
/*		
		boolean moveBack = legs.moveBackExp(currentTime, k);
		boolean rotateBack = rotateBack(currentTime, k);
		if(rotateBack){
			applyRotation();
		}
		if(rotateBack || moveBack){
			setUpdate();
		}*/
	}
	
//	@Override
	public void translateNoView(Origin<C> origin){
		this.translate(origin, false);
	}
	
	@Override
	public P print(){
		return (P) new WalkPrint(this.printStore);
	}

}
