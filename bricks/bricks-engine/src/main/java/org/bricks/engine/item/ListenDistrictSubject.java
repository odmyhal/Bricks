package org.bricks.engine.item;

import org.bricks.core.entity.type.Brick;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.ListenDistrictEntity;

public class ListenDistrictSubject<L extends ListenDistrictEntity> extends Subject<L>{

	public ListenDistrictSubject(Brick brick) {
		super(brick);
	}

	public boolean joinDistrict(District sector){
		boolean result = super.joinDistrict(sector);
		if(result){
			entity.onDistrictJoin(sector);
		}
		return result;
	}
}
