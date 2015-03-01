package org.bricks.engine.neve;

public abstract class PrintableBase<I extends Imprint> implements Printable<I>{
	
	protected PrintStore<? extends Printable, I> printStore;
	
	protected void initPrintStore(){
		printStore = new PrintStore(this);
	}
	

	public int adjustCurrentPrint(){
		return printStore.adjustCurrentPrint();
	}
	
	public I getInnerPrint(){
		return printStore.getInnerPrint();
	}
	
	public I getSafePrint(){
		return printStore.getSafePrint();
	}
	

	public boolean isLastPrint(int pNum){
		return printStore.isLastPrint(pNum);
	}
}
