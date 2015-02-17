package org.bricks.engine.view;

import java.util.LinkedList;

import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Roller;

public class RollView<R extends Roller> extends EntityView<R>{
	
	public RollView(LinkedList backet) {
		super(backet);
		// TODO Auto-generated constructor stub
	}

	private float rotationSpeed;
	private float rotation;
/*
	public RollView(R entity) {
		super(entity);
	}
*/	
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
		this.rotationSpeed = this.entity.getRotationSpeed();
		this.rotation = this.entity.getRotation();
	}
/*	protected void init(R e){
		super.init(e);
		this.rotationSpeed = e.getRotationSpeed();
		this.rotation = e.getRotation();
	}*/
	
}
