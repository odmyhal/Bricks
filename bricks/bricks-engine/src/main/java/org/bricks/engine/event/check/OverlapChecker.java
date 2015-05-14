package org.bricks.engine.event.check;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.pool.Area;
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
			Subject last = targetP.checkLastOverlap();
			curState.setLast(last);
		}
		int entityNum = curState.getEntityState();
		while(entityNum < targetP.getStaff().size()){
			Subject target = (Subject) targetP.getStaff().get(entityNum);
			Area area = target.getDistrict().getBuffer();
			int areaNum = curState.getAndIncrementAreaState();
//			targetP.log(" 1.1 Checks " + areaNum + " of " + area.capacity() + ", my center is: " + target.getCenter());
			while(areaNum < area.capacity()){
				Subject<?, ?, ?, ?> sv = area.getSubject(areaNum);
				areaNum = curState.getAndIncrementAreaState();
				if(sv == null || sv == target || sv == curState.getLast()){
//					targetP.log(" 1.2 Wrong slot because of " + 
//							(sv == null ? "null" : sv == target ? "me" : sv == curState.getLast() ? "last" : "none"));
					continue;
				}
//				targetP.log("  2. Before Liver checkOverlap");
				OverlapEvent resultEvent = targetP.checkOverlap(target, sv);
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
