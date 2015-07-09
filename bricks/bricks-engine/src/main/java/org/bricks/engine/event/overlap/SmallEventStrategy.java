package org.bricks.engine.event.overlap;

import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;

public class SmallEventStrategy<T extends Liver, H extends Habitant, TD, HD, P> extends OverlapStrategy<T, H, TD, HD, P>{

	public SmallEventStrategy(OverlapAlgorithm<TD, HD, P> algorithm, 
			OverlapStrategy.HabitantDataExtractor<H, HD> extractor, OverlapStrategy.EventProducer<TD, HD, P> producer){
		super(algorithm, extractor, producer);
	}
	
	public boolean hasToCheckOverlap(T target, H source){
		EntityCore se = source.getEntity();
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
