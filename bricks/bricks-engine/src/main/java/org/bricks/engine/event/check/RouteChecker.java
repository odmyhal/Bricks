package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.Event;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Walker;
import org.bricks.exception.Validate;

public class RouteChecker<T extends Walker<?, Fpoint>/*MultiWalker<?, ?, Fpoint, ?>*/> extends LoopEventChecker<T> {

	public static final CheckerType CHECKER_TYPE = CheckerType.registerCheckerType();
	
	public RouteChecker(float rotationSpeed, float sensitiveDistance, Fpoint... route) {
		super(CHECKER_TYPE);
		this.supplant(CHECKER_TYPE);
		this.supplant(RollToMarkProcessorChecker.CHECKER_TYPE);
		Validate.isTrue(route.length > 1, "Route should comprise at least two points...");
		for(Fpoint one: route){
			this.addChecker(new RollToPointProcessorChecker(one, rotationSpeed));
			this.addChecker(new RaisePointChecker(one, sensitiveDistance));
		}
	}

	private class RaisePointChecker extends CachePointChecker<T>{

		public RaisePointChecker(Fpoint tPoint, float distance) {
			super(tPoint, distance);
		}

		@Override
		protected Event produceEvent(T target) {
			return null;
		}
	}
}
