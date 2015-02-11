package org.bricks.engine.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bricks.core.entity.impl.BrickWrap;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.view.SubjectView;
import org.bricks.core.entity.type.Brick;

public class Subject<E extends Entity> extends BrickWrap implements Satellite{
	
	private final Map<AreaBase, Integer> pools = new HashMap<AreaBase, Integer>();
	private District<?, E> district;
	private int sectorMask;
	protected E entity;

	private SubjectView currentView;
	private final LinkedList<SubjectView> viewCache = new LinkedList<SubjectView>();

	public Subject(Brick brick) {
		super(brick);
	}
/*	
	public Subject(Brick brick, Point origin){
		super(brick, origin);
	}
*/	
	public void setEntity(E e){
		this.entity = e;
	}
	
	public E getEntity(){
		return entity;
	}
	
	public District<?, E> getDistrict() {
		return district;
	}

	public boolean inPool(Pool pool){
		return pools.containsKey(pool);
	}

	public boolean joinPool(AreaBase pool){
		if(pools.containsKey(pool)){
			return false;
		}
		pools.put(pool, pool.addSubject(this));
		return true;
	}
	
	public boolean leavePool(AreaBase pool){
		Integer reslt = pools.remove(pool);
		if(reslt == null){
			return false;
		}
		pool.freeSubject(reslt);
		return true;
	}
	
	public boolean joinDistrict(District<?, E> sector){
		boolean result = joinPool(sector);
		if(result){
			this.district = sector;
			joinPool(sector.getBuffer());
			SectorMonitor.monitor(this);
		}
		return result;
	}
	
	public boolean leaveDistrict(){
//condition is not appropriatable for Stone
//		Validate.isTrue(this.district != null, "Subject is out of sector");
		boolean result = leavePool(this.district);
		if(result){
			this.district = null;
		}
		for(AreaBase ar : pools.keySet()){
			ar.freeSubject(pools.get(ar));
		}
		pools.clear();
		setDistrictMask(0);
		return result;
	}
/*	
	public void monitorSectorPosition(){
		SectorMonitor.monitor(this);
	}
*/	
	public int getDistrictMask() {
		return sectorMask;
	}

	public void setDistrictMask(int sectorMask) {
		this.sectorMask = sectorMask;
	}
	
	public LinkedList<SubjectView> getViewCache(){
		return this.viewCache;
	}
	
//	public static final AtomicLong createdView = new AtomicLong(0);
//	public static final AtomicLong reusedView = new AtomicLong(0);
	
	public void adjustCurrentView(){
		synchronized(viewCache){
			SubjectView nView = viewCache.pollFirst();
			if(nView == null){
				nView = new SubjectView(this);
//				createdView.incrementAndGet();
			}else{
				nView.init();
			}
			nView.occupy();
			if(currentView != null){
				currentView.free();
			}
			currentView = nView;
		}
	}
	
	public SubjectView getCurrentView(){
//		synchronized(viewCache){
			return currentView;
//		}
	}
	
	public SubjectView getOccupiedCurrentView(){
		synchronized(viewCache){
			currentView.occupy();
			return currentView;
		}
	}
	
	public void update() {
		SectorMonitor.monitor(this);
		this.adjustCurrentView();
	}
	
//	public abstract void applyEngine(Engine engine);
}
