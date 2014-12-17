package org.bricks.engine.item;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.pool.SectorMonitor;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Roll;
import org.bricks.engine.tool.Walk;
import org.bricks.engine.view.WalkView;

public abstract class MultiWalker<S extends Subject> extends MultiRoller<S> implements Walker{

	private Walk legs;

	protected MultiWalker() {
//		legs = new Walk(this);
	}
	
	@Override
	protected void init(){
		legs = new Walk(this);
		super.init();
	}
	
	public void motorProcess(long currentTime){
		processEvents(currentTime);
		if(!alive()){
			return;
		}
		boolean rotate = rotate(currentTime);
		if(rotate){
			applyRotation();
		}
		boolean move = legs.move(currentTime);
		if(rotate || move){
			adjustCurrentView(false);
			for(Satellite satellite : getSatellites()){
				satellite.update();
			}
/*			for(S subject : getStaff()){
				SectorMonitor.monitor(subject);
				subject.adjustCurrentView();
			}*/
		}
	}
	
	@Override
	public void flushTimer(long nTime){
		super.flushTimer(nTime);
		legs.flushTimer(nTime);
	}

	public void setVector(Fpoint vector) {
		legs.setVector(vector);
	}
	
	public void setVector(float x, float y){
		legs.setVector(x, y);
	}

	public Fpoint getVector() {
		return legs.getVector();
	}

	@Override
	public void rollBack(long currentTime){
		boolean moveBack = legs.moveBack(currentTime);
		boolean rotateBack = rotateBack(currentTime);
		if(rotateBack){
			applyRotation();
		}
		if(rotateBack || moveBack){
			adjustCurrentView(false);
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
	public WalkView provideCurrentView(){
		return new WalkView(this);
	}
}
