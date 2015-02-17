package org.bricks.engine.neve;

public interface Printable<I extends Imprint> {

	public void adjustCurrentPrint();
	
	public I getInnerPrint();
	
	public I getSafePrint();
	
	public I print();
}
