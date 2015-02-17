package org.bricks.engine.staff;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.District;

public interface ListenDistrictEntity<I extends Imprint> extends Entity<I>{

	public void onDistrictJoin(District d);
}
