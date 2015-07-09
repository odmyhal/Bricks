package org.bricks.engine.staff;

import org.bricks.core.entity.Point;
import org.bricks.engine.pool.AreaBase;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Pool;

public interface Habitant<E extends EntityCore> {

	public District<E> getDistrict();

	public boolean inPool(Pool pool);

	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean joinPool(AreaBase pool);
	
	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean leavePool(AreaBase pool);
	
	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean joinDistrict(District<E> sector);

	/*
	 * Method should occur only in motor thread
	 * or before engine.start()
	 */
	public boolean leaveDistrict();
	

	public void moveToDistrict(District<E> newOne);

	public int getDistrictMask();

	public void setDistrictMask(int sectorMask);
	

	public Point getCenter();

	public void setEntity(E e);
	
	public E getEntity();
	
}
