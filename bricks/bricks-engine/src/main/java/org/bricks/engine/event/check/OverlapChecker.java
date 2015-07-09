package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.pool.Area;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Liver;

public class OverlapChecker<T extends Liver> extends EventChecker<T>{
	
	private static final OverlapChecker instance = new OverlapChecker();
	private final ThreadLocal<EventCheckState> localCheckState = new ThreadLocal<EventCheckState>() {
        @Override protected EventCheckState initialValue() {
            return new EventCheckState();
        }
    };
	
	private OverlapChecker(){
		super(CheckerType.registerCheckerType());
	};
	
	public static OverlapChecker instance(){
		return instance;
	}
	
	public boolean isActive(){
		return true;
	}
	
	public void activate(T target, long curTime){}
	
	protected Event popEvent(Liver targetP, long eventTime){
//		targetP.log("  1. Overlap checker pop event");
		EventCheckState curState = localCheckState.get();
		if(curState.checkNew(targetP)){
//			Subject last = targetP.checkLastOverlap();
//			curState.setLast(last);
		}
		int entityNum = curState.getEntityState();
		while(entityNum < targetP.getStaff().size()){
			Subject target = (Subject) targetP.getStaff().get(entityNum);
			Area area = target.getDistrict().getBuffer();
			int areaNum = curState.getAndIncrementAreaState();
			while(areaNum < area.capacity()){
				Habitant<?> habitant = area.getSubject(areaNum);
				areaNum = curState.getAndIncrementAreaState();
				if( habitant == null || habitant == target/* || habitant == curState.getLast()*/){
					continue;
				}
				OverlapEvent resultEvent = targetP.checkOverlap(target, habitant);
				if(resultEvent == null){
					continue;
				}
				return resultEvent;
			}
			entityNum = curState.incrementEntityState();
		}
		curState.reject();
		return null;
	}


}
