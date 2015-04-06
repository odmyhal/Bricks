package org.bricks.engine.neve;

import org.bricks.engine.staff.Roller;

public class RollPrint<P extends Roller, C> extends EntityPrint<P, C> {
	
	private float rotationSpeed;
	private float rotation;

	public RollPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	public float getRotationSpeed(){
		return rotationSpeed;
	}
	
	public float getRotation(){
		return rotation;
	}

	public String toString(){
		return String.format("RollView(origin=%s, rotationSpeed=%.5f)", this.getOrigin(), this.getRotationSpeed());
	}
	
	@Override
	public void init(){
		super.init();
		this.rotationSpeed = this.getTarget().getRotationSpeed();
		this.rotation = this.getTarget().getRotation();
	}
}
