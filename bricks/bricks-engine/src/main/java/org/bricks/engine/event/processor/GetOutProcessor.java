package org.bricks.engine.event.processor;

import org.bricks.engine.staff.Liver;

public class GetOutProcessor extends ImmediateActProcessor{
	
	private static final GetOutProcessor instance = new GetOutProcessor();
	
	private GetOutProcessor(){}
	
	public static GetOutProcessor instance(){
		return instance;
	}

	@Override
	protected void processSingle(Liver target, long processTime) {
		target.outOfWorld();
//		System.out.println(target.getClass().getCanonicalName() + " get out of world");
	}

}
