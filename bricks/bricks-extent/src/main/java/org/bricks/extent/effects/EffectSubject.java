package org.bricks.extent.effects;

import java.util.ArrayList;

import org.bricks.core.entity.Point;
import org.bricks.exception.NotSupportedMethodException;
import org.bricks.exception.Validate;
import org.bricks.extent.space.Origin3D;
import org.bricks.extent.space.Roll3D;
import org.bricks.engine.Engine;
import org.bricks.engine.Motor;
import org.bricks.engine.item.Motorable;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.AreaBase;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Pool;
import org.bricks.engine.pool.Tenant;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;

import com.badlogic.gdx.math.Vector3;

public class EffectSubject implements Habitant, EntityCore, Motorable{
	
	private Tenant tenant;
	private TemporaryEffect effect;
	private Engine engine;
	
	public EffectSubject(){
		tenant = new Tenant(this);
	}


	public void joinWorld(World world) {
		District d = world.pointSector(this.getCenter());
		Validate.isFalse(d == null, "Could not find district for point: " + this.getCenter());
		this.joinDistrict(d);
	}

	public String sourceType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void applyEngine(Engine engine) {
		this.engine = engine;
		World world = engine.getWorld();
		joinWorld(world);
		Motor motor = engine.getLazyMotor();
		motor.addLiver(this);
	}


	public void timerSet(long time) {
		// TODO Auto-generated method stub
		
	}


	public void timerAdd(long time) {
		// TODO Auto-generated method stub
		
	}


	public void motorProcess(long currentTime) {
		// TODO Auto-generated method stub
		
	}


	public boolean alive() {
		// TODO Auto-generated method stub
		return false;
	}


	public Engine getEngine() {
		// TODO Auto-generated method stub
		return null;
	}


	public void disappear() {
		// TODO Auto-generated method stub
		
	}


	public void outOfWorld() {
		// TODO Auto-generated method stub
		
	}


	public District getDistrict() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean inPool(Pool pool) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean joinPool(AreaBase pool) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean leavePool(AreaBase pool) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean joinDistrict(District sector) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean leaveDistrict() {
		// TODO Auto-generated method stub
		return false;
	}


	public void moveToDistrict(District newOne) {
		// TODO Auto-generated method stub
		
	}


	public int getDistrictMask() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void setDistrictMask(int sectorMask) {
		// TODO Auto-generated method stub
		
	}


	public Point getCenter() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setEntity(EntityCore e) {
		// TODO Auto-generated method stub
		
	}


	public EntityCore getEntity() {
		return this;
	}

}
