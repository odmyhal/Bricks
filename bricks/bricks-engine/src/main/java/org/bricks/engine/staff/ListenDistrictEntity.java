package org.bricks.engine.staff;

import org.bricks.engine.pool.District;

public interface ListenDistrictEntity extends Entity{

	public void onDistrictJoin(District d);
}
