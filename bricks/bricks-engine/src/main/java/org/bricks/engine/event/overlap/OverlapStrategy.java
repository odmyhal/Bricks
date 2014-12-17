package org.bricks.engine.event.overlap;

import org.bricks.engine.pool.Subject;

public abstract class OverlapStrategy {

	public static final OverlapStrategy TRUE = new TrueOverlapStrategy();
	public static final OverlapStrategy FALSE = new FalseOverlapStrategy();
	
	public abstract boolean hasToCheckOverlap(Subject source);
	
	private static class FalseOverlapStrategy extends OverlapStrategy{

		@Override
		public boolean hasToCheckOverlap(Subject source) {
			return false;
		}
		
	}
	
	private static class TrueOverlapStrategy extends OverlapStrategy{

		@Override
		public boolean hasToCheckOverlap(Subject source) {
			return true;
		}
		
	}
}
