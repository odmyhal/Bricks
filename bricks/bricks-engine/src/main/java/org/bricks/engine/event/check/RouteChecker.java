package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.Event;
import org.bricks.engine.item.MultiRoller;
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
		initRoutes(route, rotationSpeed, sensitiveDistance);
	}
	
	protected void initRoutes(Fpoint[] routes, float rotationSpeed, float sensitiveDistance){
		for(int i=0; i<routes.length; i++){
			this.addChecker(new RollToPointProcessorChecker(routes[i], rotationSpeed));
			this.addChecker(new RaisePointChecker(routes[i], sensitiveDistance));
		}
	}

	protected class RaisePointChecker extends CachePointChecker<T>{

		public RaisePointChecker(Fpoint tPoint, float distance) {
			super(tPoint, distance);
		}

		@Override
		protected Event produceEvent(T target) {
			return null;
		}
	}
	
}
