package org.bricks.engine.item;

import org.bricks.core.entity.Point;
import org.bricks.engine.pool.SectorMonitor;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.view.RollView;

public abstract class MultiRoller<S extends Subject> extends MultiLiver<S> implements Roller{
	
	private Roll roll;

	protected MultiRoller() {
	}
	
	@Override
	protected void init(){
		roll = new Roll();
		super.init();
	}

//	@Override
	public float getRotationSpeed() {
		return roll.getRotationSpeed();
	}

//	@Override
	public void setRotationSpeed(float rotationSpeed) {
		roll.setRotationSpeed(rotationSpeed);
		this.adjustCurrentView();
	}

//	@Override
	public float getRotation() {
		return roll.getRotation();
	}
	
	public float getSafeRotation(){
		synchronized(viewCache){
			return ((RollView)view).getRotation();
		}
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
			this.adjustCurrentView(false);
			for(Satellite satellite : getSatellites()){
				satellite.rotate(roll.getRotation(), getOrigin());
				satellite.update();
			}
/*			for(S subject : getStaff()){
				subject.rotate(roll.getRotation(), getOrigin());
				SectorMonitor.monitor(subject);
				subject.adjustCurrentView();
			}*/
		}
	}

	public void applyRotation(){
		for(Satellite satellite : getSatellites()){
			satellite.rotate(roll.getRotation(), getOrigin());
		}
/*		for(S subject : getStaff()){
			subject.rotate(roll.getRotation(), getOrigin());
		}*/
	}
	
	protected boolean rotateBack(long currentTime){
		return roll.rotateBack(currentTime);
	}
	
	public void rollBack(long currentTime){
		if(roll.rotateBack(currentTime)){
			this.adjustCurrentView(false);
			for(Satellite satellite : getSatellites()){
				satellite.rotate(roll.getRotation(), getOrigin());
				satellite.update();
			}
/*			for(S subject : getStaff()){
				subject.rotate(roll.getRotation(), getOrigin());
				SectorMonitor.monitor(subject);
				subject.adjustCurrentView();
			}*/
		}
	}
	
	@Override
	public RollView provideCurrentView(){
		return new RollView(this);
	}
}
