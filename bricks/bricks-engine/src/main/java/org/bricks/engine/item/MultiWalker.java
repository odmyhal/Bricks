package org.bricks.engine.item;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.pool.SectorMonitor;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.tool.Walk;
import org.bricks.engine.view.WalkView;
import org.bricks.exception.Validate;

public abstract class MultiWalker<S extends Subject, P extends WalkPrint> extends MultiRoller<S, P> implements Walker<P>{

	private Walk legs;
	private Fpoint vector;
	private float acceleration;
	private long accelerationTime;
/*
	protected MultiWalker() {
		legs = new Walk(this);
		vector = new Fpoint(0f, 0f);
	}
*/	
	@Override
	protected void init(){
		legs = new Walk(this);
		vector = new Fpoint(0f, 0f);
		super.init();
	}
	
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
		boolean move = legs.move(currentTime, vector.getFX(), vector.getFY());
		if(rotate || move){
			adjustCurrentPrint(false);
			for(Satellite satellite : getSatellites()){
				satellite.update();
			}
		}
	}
	
	private void applyAcceleration(long curTime){
		if(acceleration != 0){
			float diff = (curTime - accelerationTime) / 1000f;
			Validate.isTrue(diff >= 0, "Need to check accelaration time...");
			if(diff < 0.01){
//				System.out.println(curTime + " - Dmall diff " + diff);
				return;
			}
			double rotation = getRotation();
			float absAcc = acceleration * diff;
			vector.setX(vector.getFX() + absAcc * (float) Math.cos(rotation));
			vector.setY(vector.getFY() + absAcc * (float) Math.sin(rotation));
//			System.out.println(curTime + " -- AccTime " + accelerationTime + " - Up speed on " + absAcc + ", new speed " + vector.getFX() + ", diff was " + diff);
			Validate.isTrue(vector.getFX() < 10000, "Speed X is to hie");
			Validate.isTrue(vector.getFY() < 10000, "Speed Y is to hie");
			accelerationTime = curTime;
		}
	}
	
	@Override
	public void flushTimer(long nTime){
		super.flushTimer(nTime);
		legs.flushTimer(nTime);
	}

	public void setVector(Fpoint vector) {
		this.vector.setX(vector.getFX());
		this.vector.setY(vector.getFY());
	}
	
	public void setVector(float x, float y){
		this.vector.setX(x);
		this.vector.setY(y);
	}

	public Fpoint getVector() {
		return vector;
	}
	

	public void setAcceleration(float acceleration, long accTime){
		this.acceleration = acceleration;
		accelerationTime = accTime;
	}
	
	public float getAcceleration(){
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
	public void translateNoView(int x, int y){
		this.translate(x, y, false);
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
