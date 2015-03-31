package org.bricks.engine.event.overlap;

import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;

public class SmallEventStrategy extends OverlapStrategy{

	public SmallEventStrategy(OverlapAlgorithm algorithm){
		super(algorithm);
	}
	
	public boolean hasToCheckOverlap(Liver target, Subject source){
		Entity se = source.getEntity();
		if(target.equals(se)){
			return false;
		}
		int r = target.hashCode() - se.hashCode();
		if(r > 0){
			return false;
		}
		if(r < 0){
			return true;
		}
		return target.toString().compareTo(se.toString()) < 0;
	}
}
