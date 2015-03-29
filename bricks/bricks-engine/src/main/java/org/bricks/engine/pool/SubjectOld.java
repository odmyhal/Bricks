package org.bricks.engine.pool;

import java.util.HashMap;
import java.util.Map;

import org.bricks.core.entity.Point;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;
import org.bricks.exception.Validate;

@Deprecated
public abstract class SubjectOld<E extends Entity, I extends Imprint, C, R extends Roll> extends PrintableBase<I> implements Satellite<C, R>{
	
	private final Map<AreaBase, Integer> pools = new HashMap<AreaBase, Integer>();
	private District<?, E> district;
	private int sectorMask;
	protected E entity;
/*	
	public Subject(Brick brick, Point origin){
		super(brick, origin);
	}
*/	
	
	public SubjectOld(){
		this.initPrintStore();
	}
	
	public abstract Point getCenter();

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

	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean joinPool(AreaBase pool){
		if(pools.containsKey(pool)){
			return false;
		}
		//Old comment		pools.put(pool, pool.addSubject(this));
		return true;
	}
	
	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean leavePool(AreaBase pool){
		Integer reslt = pools.remove(pool);
		if(reslt == null){
			return false;
		}
		pool.freeSubject(reslt);
		return true;
	}
	
	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean joinDistrict(District<?, E> sector){
		boolean result = joinPool(sector);
		if(result){
			this.district = sector;
			joinPool(sector.getBuffer());
			//Old comment			SectorMonitor.monitor(this);
		}
		return result;
	}

	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
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
	
	public void moveToDistrict(District<?, E> newOne){
		if(district.equals(newOne)){
			System.out.println("Wrong district found for point: " + this.getCenter());
		}
		Validate.isTrue(!(district.equals(newOne)));
		District<?, E> oldOne = district;
		int oldDistrictNum = -1;
		for(AreaBase ar : pools.keySet()){
			if(ar == district){
				oldDistrictNum = pools.get(ar);
			}else{
				ar.freeSubject(pools.get(ar));
			}
		}
		pools.clear();
		setDistrictMask(0);
		this.joinDistrict(newOne);
		oldOne.freeSubject(oldDistrictNum);
	}

	public int getDistrictMask() {
		return sectorMask;
	}

	public void setDistrictMask(int sectorMask) {
		this.sectorMask = sectorMask;
	}

	public void update() {

//Old comment		SectorMonitor.monitor(this);
		this.adjustCurrentPrint();
	}
	
}
