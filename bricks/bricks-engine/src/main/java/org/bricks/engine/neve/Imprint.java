package org.bricks.engine.neve;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.exception.Validate;

public abstract class Imprint<P extends Printable> {
	
	protected PrintStore<P, ?> printStore;
	private AtomicInteger counter = new AtomicInteger(0);
	
	public Imprint(PrintStore<P, ?> ps){
		printStore = ps;
	}
	
	protected void checkCounterZero(){
		Validate.isTrue(counter.get() == 0, "Counter should be zero");
	}

	protected abstract void init();
	
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
	
	protected final void occupyFirst(){
		int cnt = 0;
		while(!counter.compareAndSet(0, 1)){
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
	
	protected void endUse(){
		printStore.putPrint(this);
	}
	
	public P getTarget(){
		return printStore.target;
	}
}
