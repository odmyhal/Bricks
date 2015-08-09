package org.bricks.engine.neve;

import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;

public class WalkLastPrint<P extends Walker<?, C>, C> extends WalkPrint<P, C> {
	
	private float lastRotation;
	private Origin<C> lastMovement;

	public WalkLastPrint(PrintStore<P, ?> ps) {
		super(ps);
		lastMovement = printStore.target.provideInitialOrigin();
	}
	
	@Override
	public void init(){
		super.init();
		lastRotation = printStore.target.lastRotation();
		lastMovement.set(printStore.target.lastMove());
	}
	
	public C lastMove(){
		return lastMovement.source;
	}

	public float lastRotation(){
		return lastRotation;
	}
}
