package org.bricks.engine.neve;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.exception.Validate;

public final class PrintStore<T extends Printable, P extends Imprint> {
	
	private static final int size = 5;

	protected T target;
	private PrintSlot<P>[] cache = new PrintSlot[size];
	private AtomicInteger putIndex = new AtomicInteger(-1);
	/**
	 * getIndex has no need to be volatile if isLastPrint accept volatile parameter
	 */
	private /*volatile*/ int getIndex = 0;
	private int serialPrintNumber = 0;
	private volatile P currentPrint;
	
	public PrintStore(T printable){
		this.target = printable;
		initSlots();
//		initPrint();
	}
	
	private final void initSlots(){
		for(int i=0; i<size; i++){
			cache[i] = new PrintSlot();
		}
	}
	
	private final void initPrint(){
		P newPrint = getPrint();
		newPrint.intStoreInitialization();
//		newPrint.checkCounterZero();
//		newPrint.init();
//		newPrint.occupyFirst();
		currentPrint = newPrint;
	}
	
	/*
	 * Runs only in motor thread. getIndex is thread local
	 */
	public int adjustCurrentPrint(){
		P oldPrint = currentPrint;
		initPrint();
		if(oldPrint != null){
			oldPrint.free();
		}
		return serialPrintNumber;
	}
	
	/*
	 * Use this method only in native motor
	 */
	public P getInnerPrint(){
		return currentPrint;
	}
	
	public P getSafePrint(){
		P res = null;
		int cnt = 0;
		while(res == null){
			res = currentPrint;
			if(!res.occupy()){
				res = null;
				Validate.isTrue(++cnt < 100, "Could not occupy " + this.getClass().getCanonicalName());
				Thread.currentThread().yield();
			}
		}
		return res;
	}
	
	/*
	 * Because of getIndex is not volatile, parameter printNum should be volatile
	 */
	public boolean isLastPrint(int printNum){
		return printNum == serialPrintNumber;
	}
	
	protected void putPrint(Imprint print){
		int putNum = putIndex.incrementAndGet() % size;
		cache[putNum].set((P)print);
	}
	
	/*
	 * Runs only in motor thread. getIndex is thread local
	 */
	private P getPrint(){
		int getNum = getIndex % size;
		P print = cache[getNum].get();
		++serialPrintNumber;
		if(print == null){
			return (P) target.print();
		}else{
			++getIndex;
		}
		return print;
	}
	
	private class PrintSlot<PR extends Imprint>{
		
		private volatile PR print;
		
		private void set(PR print){
			this.print = print;
		}
		
		private PR get(){
			PR tmp = this.print;
			if(tmp != null){
				this.print = null;
			}
			return tmp;
		}
		
	}
}
