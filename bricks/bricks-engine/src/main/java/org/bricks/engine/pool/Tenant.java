package org.bricks.engine.pool;

import java.util.HashMap;
import java.util.Map;

import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Subject;
import org.bricks.exception.Validate;

public class Tenant<E extends Entity> {

	private final Map<AreaBase, Integer> pools = new HashMap<AreaBase, Integer>();
	private District<?, E> district;
	private int sectorMask;
	private Subject<?, ?, ?, ?> subject;
	
	public Tenant(Subject subject){
		this.subject = subject;
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
		pools.put(pool, pool.addSubject(subject));
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
			SectorMonitor.monitor(subject);
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
			System.out.println("Wrong district found for point: " + subject.getCenter());
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
	
}
