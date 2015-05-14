package org.bricks.engine.neve;

public interface PrintFactory<I extends Imprint> {

	public I producePrint(PrintStore<? extends Printable, I> printStore);
}
