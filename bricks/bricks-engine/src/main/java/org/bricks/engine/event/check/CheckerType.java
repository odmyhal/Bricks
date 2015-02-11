package org.bricks.engine.event.check;

import java.util.concurrent.atomic.AtomicInteger;

public class CheckerType {
	
	private static final AtomicInteger generator = new AtomicInteger(0);
	public static final CheckerType NO_SUPLANT = registerCheckerType();

	private int identity;
	
	private CheckerType(int identity){
		this.identity = identity;
	}
	
	public static CheckerType registerCheckerType(){
		return new CheckerType(generator.incrementAndGet());
	}
}
