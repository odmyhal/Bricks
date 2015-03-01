package org.bricks.engine.neve;

public interface Printable<I extends Imprint> {

	public int adjustCurrentPrint();
	
	public I getInnerPrint();
	
	public I getSafePrint();
	
	public I print();
	
	public boolean isLastPrint(int pNum);
}
