package org.bricks.engine.event.check;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.RouteChecker.RaisePointChecker;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Walker;

public class DurableRouteChecker<T extends DurableRouteChecker.DurableRoutedWalker<?>> extends RouteChecker<T>{


	public DurableRouteChecker(float rotationSpeed, float sensitiveDistance, Fpoint... route) {
		super(rotationSpeed, sensitiveDistance, route);
	}
	
	protected void initRoutes(Fpoint[] routes, float rotationSpeed, float sensitiveDistance){
		for(int i=0; i<routes.length; i++){
			this.addChecker(new RollToPointSensetiveProcessor(routes[i], rotationSpeed, sensitiveDistance));
			this.addChecker(new RaisePointChecker(routes[i], sensitiveDistance));
		}
	}

	@Override
	protected Event popEvent(T target, long eventTime){
		if(target.correctRoute(eventTime)){
			if(this.nextIndex % 2 == 0){
				this.indexBack();
			}
			this.restartCurrentChecker();
		}
		return super.popEvent(target, eventTime);
	}


	public static interface DurableRoutedWalker<I extends WalkPrint> extends Walker<I, Fpoint>{
		
		public boolean correctRoute(long curTime);
	}
}
