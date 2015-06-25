package org.bricks.extent.processor;

import java.util.ArrayList;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.check.ChunkEventChecker;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.processor.WorkToConditionProcessor;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.tool.Roll;
import org.bricks.exception.Validate;
import org.bricks.extent.space.Roll3D;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Vector3;

public abstract class RollModelBrickProcessor<T extends MultiLiver<? extends ModelBrickSubject, ?, ?>> 
	extends WorkToConditionProcessor<T>{
	
	private Roll3D roll = new Roll3D();
	private float rotateRadians, rotationSpeed, targetRotation;
	private Vector3 tmpSpin;
	private boolean unchanged;

	public RollModelBrickProcessor() {
		super(CheckerType.NO_SUPLANT);
	}
	
	public RollModelBrickProcessor(CheckerType checkerType) {
		super(checkerType);
	}
	
	/**
	 * Method should be called before adding processor to Liver
	 * @param spin
	 * @param rotateRadians
	 * @param rotationSpeed
	 */
	public void init(Vector3 spin, float rotateRadians, float rotationSpeed){
		Validate.isFalse(rotationSpeed == 0f);
		this.rotationSpeed = rotationSpeed;
		this.rotateRadians = rotateRadians;
		this.tmpSpin = spin;
	}
	
	@Override
	public void activate(T target, long curTime){
		while(rotateRadians <= -Roll.rotationCycle){
			rotateRadians += Roll.rotationCycle;
		}
		while(rotateRadians >= Roll.rotationCycle){
			rotateRadians -= Roll.rotationCycle;
		}
		float startRotation = 0f;
		if(rotateRadians < 0){
			startRotation = Math.max(Roll.rotationCycle - Roll.rotationBuff, -rotateRadians);
			rotationSpeed *= -1;
		}
		targetRotation = startRotation + rotateRadians;
		roll.setRotationSpeed(rotationSpeed);
		roll.setSpin(tmpSpin, startRotation, curTime);
		unchanged = false;
		super.activate(target, curTime);
	}
	

	public void timerSet(long time){
		roll.timerSet(time);
	}
	
	public void timerAdd(long time){
		roll.timerAdd(time);
	}
	
	protected abstract Vector3 targetCenter(T target);

	@Override
	public void doJob(T target, long processTime) {
		if(roll.rotate(processTime)){
			rotateStaff(target, roll.getSpin(), roll.lastRotation());
			unchanged = false;
		}
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		if(unchanged){
			return false;
		}
		float diff = targetRotation - roll.getRotation();
		if(Math.abs(diff) < Roll.rotationBuff){
			rotateStaff(target, roll.getSpin(), diff);
			return true;
		}
		unchanged = true;
		return false;
	}
	
	private void rotateStaff(T target, Vector3 spin, float rad){
		ArrayList<? extends ModelBrickSubject> staff = target.getStaff();
		Vector3 origin = targetCenter(target);
		for(int i = 0; i < staff.size(); i++){
			staff.get(i).linkModelBrick().rotate(spin, rad, origin);
		}
		target.setUpdate();
	}

	public static class FpointCentral<T extends MultiLiver<? extends ModelBrickSubject, ?, Fpoint>> 
		extends RollModelBrickProcessor<T>{
		
		private Vector3 tmpCenter = new Vector3(0f, 0f, 0f);

		@Override
		protected Vector3 targetCenter(T target) {
			Fpoint tOrigin = target.origin().source;
			tmpCenter.x = tOrigin.x;
			tmpCenter.y = tOrigin.y;
			return tmpCenter;
		}
		
	}
	
	public static class Vector3Central<T extends MultiLiver<? extends ModelBrickSubject, ?, Vector3>> 
		extends RollModelBrickProcessor<T>{
		
		@Override
		protected Vector3 targetCenter(T target) {
			return target.origin().source;
		}
	}
}
