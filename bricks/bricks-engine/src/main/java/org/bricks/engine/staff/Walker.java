package org.bricks.engine.staff;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.tool.Origin;

public interface Walker<I extends WalkPrint, C> extends Roller<I>{

//	public void setVector(Fpoint vector);
	public void setVector(Origin<C> vector);
	public Origin<C> getAcceleration();
	public void setAcceleration(Origin<C> acc, long accTime);
	public Origin<C> getVector();
	public void translateNoView(Origin<C> origin);
	
	public Origin<C> lastMove();
	public Origin<C> origin();
}
