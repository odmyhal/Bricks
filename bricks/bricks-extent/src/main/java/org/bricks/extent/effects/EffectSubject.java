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
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;

import com.badlogic.gdx.math.Vector3;

public class EffectSubject implements Subject<Entity, Imprint, Vector3, Roll3D>, Entity<Imprint>, Motorable{
	
	private Tenant<Entity> tenant;
	private TemporaryEffect effect;
	private Engine engine;
	
	public EffectSubject(){
		tenant = new Tenant(this);
	}

	public int adjustCurrentPrint() {
		throw new NotSupportedMethodException();
	}

	public Imprint getInnerPrint() {
		throw new NotSupportedMethodException();
	}

	public Imprint getSafePrint() {
		throw new NotSupportedMethodException();
	}

	public Imprint print() {
		throw new NotSupportedMethodException();
	}

	public boolean isLastPrint(int pNum) {
		throw new NotSupportedMethodException();
	}

	public void translate(Origin vector) {
		// TODO Auto-generated method stub
	}

	public void rotate(Roll3D roll, Origin<Vector3> central) {
		// TODO Auto-generated method stub
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public Point getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEntity(Entity e) {
		// TODO Auto-generated method stub
		
	}

	public Entity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	public District<?, Entity> getDistrict() {
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

	public boolean joinDistrict(District<?, Entity> sector) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean leaveDistrict() {
		// TODO Auto-generated method stub
		return false;
	}

	public void moveToDistrict(District<?, Entity> newOne) {
		// TODO Auto-generated method stub
		
	}

	public int getDistrictMask() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setDistrictMask(int sectorMask) {
		// TODO Auto-generated method stub
		
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

	public Engine getEngine() {
		return engine;
	}

	public boolean isEventTarget() {
		return false;
	}

	public ArrayList<? extends Subject> getStaff() {
		// TODO Auto-generated method stub
		return null;
	}

	public Origin origin() {
		// TODO Auto-generated method stub
		return null;
	}

	public void disappear() {
		// TODO Auto-generated method stub
		
	}

	public void outOfWorld() {
		disappear();
	}

	public Origin provideInitialOrigin() {
		return new Origin3D();
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
		return true;
	}

}
