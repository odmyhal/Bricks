package org.bricks.engine.item;

import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;

public abstract class MultiRoller<S extends Subject<?, ?, C, R>, P extends RollPrint, C, R extends Roll> extends MultiLiver<S, P, C> implements Roller<P>{
	
	private R roll;
/*
	protected MultiRoller() {
		roll = new Roll();
	}
*/	
	@Override
	protected void init(){
		roll = initializeRoll();
		super.init();
	}
	
	protected abstract R initializeRoll();
	
	public R linkRoll(){
		return roll;
	}

	public float getRotationSpeed() {
		return roll.getRotationSpeed();
	}

	public void setRotationSpeed(float rotationSpeed) {
		roll.setRotationSpeed(rotationSpeed);
//		this.adjustCurrentView();
		this.adjustCurrentPrint();
	}

	public float getRotation() {
		return roll.getRotation();
	}
/*	
	public float getSafeRotation(){
		synchronized(viewCache){
			return ((RollView)view).getRotation();
		}
	}
*/	
	public float lastRotation(){
		return roll.lastRotation();
	}
	
	public void setRotation(float radians){
		roll.setRotation(radians);
	}
	
	public void setToRotation(float radians){
		setRotation(radians);
		applyRotation();
		/**
		 * Method is suppose to be used before applyEngine, so do not need adjustCurrentPrint
		 */
	}

	public void flushTimer(long nTime) {
		roll.flushTimer(nTime);
	}

	public boolean rotate(long checkTime) {
		return roll.rotate(checkTime);
	}

	@Override
	public void motorProcess(long currentTime){
		processEvents(currentTime);
		if(alive() && rotate(currentTime)){
			adjustInMotorPrint();
		}
	}

	protected void adjustInMotorPrint(){
		this.adjustCurrentPrint(false);
		for(Satellite satellite : getSatellites()){
			satellite.rotate(roll, this.origin());
			satellite.update();
		}
	}
	
	public void applyRotation(){
		for(Satellite satellite : getSatellites()){
			satellite.rotate(roll/*.getRotation()*/, this.origin());
		}
	}
	
	protected boolean rotateBack(long currentTime){
		return roll.rotateBack(currentTime);
	}
	
	public void rollBack(long currentTime){
		if(roll.rotateBack(currentTime)){
			this.adjustCurrentPrint(false);
			for(Satellite satellite : getSatellites()){
				satellite.rotate(roll/*.getRotation()*/, this.origin());
				satellite.update();
			}
		}
	}
	
	@Override
	public P print(){
		return (P) new RollPrint(this.printStore);
	}
	/*
	@Override
	public RollView provideCurrentView(){
		return new RollView(this);
	}
	*/
}
