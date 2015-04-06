package org.bricks.engine.item;

import org.bricks.engine.neve.OriginMovePrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;

public abstract class OriginMover<S extends Subject<?, ?, C, R>, P extends OriginMovePrint, C, R extends Roll> extends MultiWalker<S, P, C, R>{
	
	public Origin<C> lastMove;
//	private int printCount;
			
	@Override
	protected void init(){
		lastMove = this.provideInitialOrigin();
		super.init();
		super.adjustCurrentPrint();
	}	
/*	
	@Override
	public void motorProcess(long currentTime){
		super.motorProcess(currentTime);
		printCount++;
		System.out.println(System.currentTimeMillis() + " Incremented Print count is " + printCount);
	}
*/
	@Override
	protected void adjustInMotorPrint(){
		Origin<C> lastOrigin = this.getInnerPrint().getOrigin();
		if(!lastOrigin.equals(this.origin())){
			lastMove.set(this.origin());
			lastMove.sub(lastOrigin);
//			printCount = 0;
		}
		super.adjustInMotorPrint();
	}
	
/*	public int printCount(){
		return printCount;
	}
*/	
	@Override
	public P print(){
		return (P) new OriginMovePrint(this.printStore);
	}
}
