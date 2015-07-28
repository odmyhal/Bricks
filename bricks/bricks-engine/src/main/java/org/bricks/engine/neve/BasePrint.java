package org.bricks.engine.neve;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.exception.Validate;

public abstract class BasePrint<P extends Printable> implements Imprint<P>{
	
	protected PrintStore<P, ?> printStore;
	private AtomicInteger counter = new AtomicInteger(0);
	
	public BasePrint(PrintStore<P, ?> ps){
		printStore = ps;
	}
	
	//should be protected without interface
	private void checkCounterZero(){
		int cnt = 0;
		while(counter.get() != 0){
			System.out.println("WARNING: (org.bricks.engine.neve.BasePrint) Counter should be zero, but we have: " + counter.get() +
					"\n for print " + printStore.getInnerPrint().getClass().getCanonicalName() + " of target " + printStore.getInnerPrint().getTarget().getClass().getCanonicalName());
		}
		Validate.isTrue(++cnt < 3);
		Thread.currentThread().yield();
	}

	//should be protected without interface
//	public abstract void init();
	
	public final void intStoreInitialization(){
		checkCounterZero();
		init();
		occupyFirst();
	}
	
	/*
	 * Print can't be occupied after undUse method called(counter == 0 in this case)
	 */
	public final boolean occupy(){
		int cnt = counter.incrementAndGet();
		if(cnt > 1){
			return true;
		}
		counter.decrementAndGet();
		Validate.isTrue(cnt > 0);
		return false;
	}
	
	//should be protected without interface
	private final void occupyFirst(){
		int cnt = 0;
		while(!counter.compareAndSet(0, 1)){
//			System.out.println("Problems with first occypying....");
			Validate.isTrue(++cnt < 100, "Could not first occupy " + this.getClass().getCanonicalName());
			Thread.currentThread().yield();
		}
	}
	
	public final void free(){
		int cnt = counter.decrementAndGet();
		if(cnt == 0){
			endUse();
		}else{
			Validate.isTrue(cnt > 0, "You should not use free more times than occupy!");
		}
	}
	
	//should be protected without interface
	protected void endUse(){
		printStore.putPrint(this);
	}
	
	public P getTarget(){
		return printStore.target;
	}
}
