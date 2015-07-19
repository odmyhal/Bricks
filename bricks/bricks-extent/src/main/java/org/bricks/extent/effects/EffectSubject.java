package org.bricks.extent.effects;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.exception.NotSupportedMethodException;
import org.bricks.exception.Validate;
import org.bricks.engine.Engine;
import org.bricks.engine.Motor;
import org.bricks.engine.item.Motorable;
import org.bricks.engine.pool.AreaBase;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Pool;
import org.bricks.engine.pool.Tenant;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.tool.Logger;
import org.bricks.utils.Cache;
import org.bricks.utils.Quarantine;
import org.bricks.utils.ThreadTransferCache;
import org.bricks.utils.ThreadTransferCache.TransferData;
import org.bricks.extent.effects.DoubleChannelRenderData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.math.Vector3;

public class EffectSubject implements Habitant, EntityCore, Motorable{
	
	public static final String SOURCE_TYPE = "BricksEffect@bricks.extent.com";
	
	private Tenant<EffectSubject> tenant;
	private TemporaryEffect effect;
	private Engine engine;
	private Motor motor;
	private DoubleChannelRenderData renderData;
	private Fpoint location = new Fpoint();
	private Logger logger = new Logger();
	
	private static final long eadleMinTime = 500;
	
	private volatile int activeEffectIndex = 0;
	private int nonActiveIndex = 1;
	private AtomicBoolean indexBlocked = new AtomicBoolean(false);
	
	private int districtCameraHook = 0;
	private long effectTimer; 
	
	public EffectSubject(TemporaryEffect effect){
		tenant = new Tenant(this);
		this.effect = effect;
		this.renderData = new DoubleChannelRenderData(effect.getControllers().get(0), effect.subChannelRenderer);
		logger.log("Effect subject created");
	}

	public void setToTranslation(Vector3 translation){
		location.set(translation.x, translation.y);
		effect.setToTranslation(translation);
	}

	public void joinWorld(World world) {
		District d = world.pointSector(this.getCenter());
		Validate.isFalse(d == null, "Could not find district for point: " + this.getCenter());
		this.joinDistrict(d);
	}

	public String sourceType() {
		return SOURCE_TYPE;
	}

	public void applyEngine(Engine engine) {
/*		if(!indexBlocked.get()){
			System.out.println(logger.getlog());
		}*/
		Validate.isFalse(indexBlocked.get(), System.currentTimeMillis() + " effect " + this + " is blocked");
		logger.log(" effect is applying engine");
		effect.start();
		renderData.flushRenderData(0);
		renderData.substituteRendererData(0);
		renderData.setChannelDataSize(0);
		nonActiveIndex = 1;
		activeEffectIndex = 0;
		this.engine = engine;
		World world = engine.getWorld();
		joinWorld(world);
//		indexBlocked.set(false);
		motor = engine.getLazyMotor();
		motor.addLiver(this);
	}


	public void timerSet(long time) {
		effectTimer = time;
//		Arrays.fill(effectTimer, time);
	}


	public void timerAdd(long time) {
		effectTimer += time;
/*		for(int i=0; i<effectTimer.length; i++){
			effectTimer[i] += time;
		}*/
	}
	
	private boolean updateDistrictCameraHook(){
		District d = tenant.getDistrict();
		if(d == null){
			return true;
		}
		int dHook = d.getCameraShoot();
		if(districtCameraHook < dHook){
			districtCameraHook = dHook;
			return true;
		}
		return false;
	}


	public void motorProcess(long currentTime) {
		long timeDiff = currentTime - effectTimer;
		boolean cameraUpdate = updateDistrictCameraHook();
		if(cameraUpdate || timeDiff > eadleMinTime){
			if(effect.done()){
				if(this.getDistrict() == null){
					if(indexBlocked.get() == false){
						disappear();
					}
				}else{
					effect.finish();
					this.leaveDistrict();
				}
				return;
			}
			ParticleController activeController = effect.getControllers().get(0);
			float deltaTime = ((float)timeDiff) / 1000f;
			activeController.deltaTime = deltaTime;
			activeController.deltaTimeSqr = deltaTime * deltaTime;
			activeController.update();
			effectTimer += timeDiff;
			
			renderData.flushRenderData(nonActiveIndex);
			if(indexBlocked.compareAndSet(false, true)){
				logger.log(" motor blocked effect");
//				logger.log(System.currentTimeMillis() + " effect blocked in thread " + Thread.currentThread().getName());
				renderData.setChannelDataSize(activeController.particles.size);
				renderData.substituteRendererData(nonActiveIndex);
				nonActiveIndex = 1 - nonActiveIndex;
				activeEffectIndex = nonActiveIndex;
//				Validate.isTrue(indexBlocked.get());
//				indexBlocked.set(false);
				boolean set = indexBlocked.compareAndSet(true, false);
				Validate.isTrue(set);
				logger.log(" motor unblocked effect");
//				logger.log(System.currentTimeMillis() + " effect made free in thread " + Thread.currentThread().getName());
			}else if(cameraUpdate){
				--districtCameraHook;
			}
		}
	}
	
	/**
	 * Method used in render thread
	 */
	private int cnt;
	public void blockActive(){
		cnt = 0;
		while(!indexBlocked.compareAndSet(false, true)){
			Gdx.app.debug("WARNING", Thread.currentThread().getName() + ": EffectSubject " + cnt + "  try to block for render");
			Thread.currentThread().yield();
			if(cnt > 200){
				System.out.println(logger.getlog());
			}
			Validate.isTrue(++cnt < 100, System.currentTimeMillis() + " Something is wrong " + this);
		}
		logger.log(" render	Blocked effect");
//		logger.log(System.currentTimeMillis() + " effect blocked in thread " + Thread.currentThread().getName());
	}
	
	/**
	 * Method used in render thread
	 */
	public void freeActive(){
//		Validate.isTrue(indexBlocked.get(), System.currentTimeMillis() + " Somebody else made effect " + this + " free");
		boolean set =  indexBlocked.compareAndSet(true, false);
		Validate.isTrue(set);
		logger.log(" render	Unblocked effect");
//		logger.log(System.currentTimeMillis() + " effect made free in thread " + Thread.currentThread().getName());
	}
	
	public void draw(){
		effect.getControllers().get(0).draw();
	}


	public boolean alive() {
		return true;
	}


	public Engine getEngine() {
		return engine;
	}


	public void disappear() {
		tenant.leaveDistrict();
		boolean removed = motor.removeLiver(this);
		Validate.isTrue(removed);
		effect.end();
//		System.out.println(System.currentTimeMillis() + " " + this + " effect dissapeared");
	}


	public void outOfWorld() {
		disappear();
	}


	public District getDistrict() {
		return tenant.getDistrict();
	}


	public boolean inPool(Pool pool) {
		return tenant.inPool(pool);
	}


	public boolean joinPool(AreaBase pool) {
		return tenant.joinPool(pool);
	}


	public boolean leavePool(AreaBase pool) {
		return tenant.leavePool(pool);
	}


	public boolean joinDistrict(District sector) {
		this.districtCameraHook = 0;
		return tenant.joinDistrict(sector);
	}


	public boolean leaveDistrict() {
//		System.out.println(System.currentTimeMillis() + " " + this + " effect leaves district");
		return tenant.leaveDistrict();
	}


	public void moveToDistrict(District newOne) {
		tenant.moveToDistrict(newOne);
	}


	public int getDistrictMask() {
		return tenant.getDistrictMask();
	}


	public void setDistrictMask(int sectorMask) {
		tenant.setDistrictMask(sectorMask);
	}


	public Point getCenter() {
		return location;
	}


	public void setEntity(EntityCore e) {
		throw new NotSupportedMethodException("Entity of EffectSubject is this. You may rewrite this method in descendant classes.");
	}


	public EntityCore getEntity() {
		return this;
	}

	public static class Transfered extends EffectSubject implements ThreadTransferCache.TransferData {
		
		private Quarantine<? extends TransferData> portal;
		private final String cacheName;

		public Transfered(TemporaryEffect effect){
			this(effect, Cache.DEFAULT_CACHE_NAME);
		}
		
		public Transfered(TemporaryEffect effect, String cacheName) {
			super(effect);
			this.cacheName = cacheName;
		}

		public void setPortal(Quarantine<? extends TransferData> portal) {
			this.portal = portal;
		}

		public Quarantine<TransferData> getPortal() {
			return (Quarantine<TransferData>) portal;
		}

		public void disappear() {
			super.disappear();
			Cache.put(cacheName, this);
		}
	}

}
