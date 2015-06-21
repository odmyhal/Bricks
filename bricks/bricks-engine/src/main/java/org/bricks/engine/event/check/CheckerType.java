package org.bricks.engine.event.check;

import java.util.concurrent.atomic.AtomicInteger;

public class CheckerType {
	
	private static final AtomicInteger generator = new AtomicInteger(0);
	public static final CheckerType NO_SUPLANT = registerCheckerType();

	private final int type;
	
	protected CheckerType(CheckerType prototype){
		this.type = prototype.type;
	}
	
	private CheckerType(int id){
		this.type = id;
	}
	
	public static CheckerType registerCheckerType(){
		return new CheckerType(generator.incrementAndGet());
	}
	
	public boolean match(CheckerType chType){
		return this.type == chType.type;
	}
}
