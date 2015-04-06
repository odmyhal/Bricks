package org.bricks.extent.space.check;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.processor.Processor;
import org.bricks.engine.item.MultiWalker;
import org.bricks.extent.space.SpaceWalker;

import com.badlogic.gdx.math.Vector3;

public class CheckInWorldProcessor<T extends MultiWalker<?, ?, Vector3, ?>> extends Processor<T>{
	
	private int maxWorld, minWorld;
	public static final CheckerType CHTYPE = CheckerType.registerCheckerType();
	
	public CheckInWorldProcessor(int min, int max){
		super(CHTYPE);
		maxWorld = max;
		minWorld = min;
	}

	@Override
	public void process(T target, long processTime) {
		Vector3 org = target.origin().source;
		if(org.z > maxWorld || org.z < minWorld){
			target.outOfWorld();
		}
	}

}
