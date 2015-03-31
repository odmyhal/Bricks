package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.staff.Subject;

public abstract class OverlapStrategy<T extends Liver> {

//	public static final OverlapStrategy TRUE = new TrueOverlapStrategy();
//	public static final OverlapStrategy FALSE = new FalseOverlapStrategy();
	private OverlapAlgorithm algorithm;
	
	public OverlapStrategy(OverlapAlgorithm oa){
		this.algorithm = oa;
	}
	
	public abstract boolean hasToCheckOverlap(T target, Subject source);
	
	public OverlapEvent checkForOverlapEvent(Imprint targetSubjectPrint, Imprint clientSubjectPrint){
		return algorithm.checkOverlap(targetSubjectPrint, clientSubjectPrint);
	}
	
	public boolean isOverlap(Imprint targetSubjectPrint, Imprint clientSubjectPrint){
		return algorithm.isOvarlap(targetSubjectPrint, clientSubjectPrint);
	}
/*	
	private static class FalseOverlapStrategy extends OverlapStrategy{

		public FalseOverlapStrategy() {
			super(null);
		}

		@Override
		public boolean hasToCheckOverlap(Liver target, Subject source) {
			return false;
		}
		
	}
*/	
	public static class TrueOverlapStrategy extends OverlapStrategy{

		public TrueOverlapStrategy(OverlapAlgorithm oa) {
			super(oa);
		}

		@Override
		public boolean hasToCheckOverlap(Liver target, Subject source) {
			return true;
		}
		
	}
}
