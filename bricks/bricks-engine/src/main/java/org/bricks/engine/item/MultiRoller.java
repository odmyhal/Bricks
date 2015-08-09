package org.bricks.engine.item;

import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;

public abstract class MultiRoller<S extends Subject<?, ?, C, R>, P extends RollPrint, C, R extends Roll> extends MultiLiver<S, P, C> implements Roller<P>{
	
	private R roll;
	
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
	
	public void timerSet(long time){
		super.timerSet(time);
		roll.timerSet(time);
	}
	
	public void timerAdd(long time){
		super.timerAdd(time);
		roll.timerAdd(time);
	}

	public boolean rotate(long checkTime) {
		return roll.rotate(checkTime);
	}

	protected void innerProcess(long currentTime){
		if(rotate(currentTime)){
			applyRotation();
			setUpdate();
		}
	}

	public void applyRotation(){
		for(int i = 0; i < satellites.size(); i++){
			satellites.get(i).rotate(roll, this.origin());
		}
	}
	
	protected boolean rotateBack(long currentTime, float k){
		return roll.rotateBack(currentTime, k);
	}
	
	public void rollBack(long currentTime, float k){
		if(roll.rotateBack(currentTime, k)){
			applyRotation();
			setUpdate();
		}
	}
	
	public void rollBack(long currentTime){
		this.rollBack(currentTime, 1f);
	}
	
	@Override
	public P print(){
		return (P) new RollPrint(this.printStore);
	}

}
