package org.bricks.engine.event.overlap;

import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;

public class SmallEventStrategy extends OverlapStrategy{

	private Liver target;
	
	public SmallEventStrategy(Liver packet){
		this.target = packet;
	}
	
	public boolean hasToCheckOverlap(Subject source){
		Entity se = source.getEntity();
		if(target.equals(se)){
			return false;
		}
		float r = target.getWeight() - se.getWeight();
		if(r > 0){
			return false;
		}
		if(r < 0){
			return true;
		}
		return target.toString().compareTo(se.toString()) < 0;
	}
}
