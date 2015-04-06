package org.bricks.engine.neve;

import org.bricks.engine.item.OriginMover;
import org.bricks.engine.tool.Origin;

public class OriginMovePrint<P extends OriginMover, C> extends WalkPrint<P, C> {

	public final Origin<C> lastMove;
	
	public OriginMovePrint(PrintStore<P, ?> ps) {
		super(ps);
		lastMove = printStore.target.provideInitialOrigin();
	}

	@Override
	public void init(){
		super.init();
		lastMove.set(printStore.target.lastMove);
	}
}
