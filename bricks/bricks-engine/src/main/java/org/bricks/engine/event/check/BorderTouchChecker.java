package org.bricks.engine.event.check;

import java.util.Collection;
import java.util.Iterator;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.core.help.SideLocator;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.BorderEvent;
import org.bricks.engine.event.Event;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.staff.Liver;

public class BorderTouchChecker<T extends Liver> extends EventChecker<T>{
	
	private static final BorderTouchChecker instance = new BorderTouchChecker();
	
	private final ThreadLocal<EventCheckState> localCheckState = new ThreadLocal<EventCheckState>() {
        @Override protected EventCheckState initialValue() {
            return new EventCheckState();
        }
    };

	private BorderTouchChecker(){
		super(CheckerType.registerCheckerType());
	};
	
	public static BorderTouchChecker instance(){
		return instance;
	}
	

	public boolean isActive(){
		return true;
	}
	
	public void activate(T target, long curTime){}

	@Override
	protected Event popEvent(Liver targetP, long eventTime) {
		EventCheckState eventState = localCheckState.get();
		eventState.checkNew(targetP);
		int entityNum = eventState.getEntityState();
		while(entityNum < targetP.getStaff().size()){
			BrickSubject target = (BrickSubject) targetP.getStaff().get(entityNum);
			bloop: while(true){
				int borderNum = eventState.getAndIncrementAreaState();
				Boundary border = target.getDistrict().getBoundary(borderNum);
				if(border == null){
					break bloop;
				}
				BorderEvent event = checkTouch(target, border);
				if(event != null){
					return event;
				}
/*				boolean isLast = checkIntHistory(targetP, border);
				if(event == null){
					if(isLast){
						targetP.removeHistory(BaseEvent.touchEventCode);
					}
				}else if(!isLast){
					targetP.putHistory(event);
					return event;
				}*/
			}
			entityNum = eventState.incrementEntityState();
		}
		eventState.reject();
		return null;
	}
	
	private BorderEvent checkTouch(BrickSubject<?, ? extends EntityPointsPrint> target, Boundary border){
		EntityPointsPrint sv = target.getInnerPrint();
		if(sv == null){
			return null;
		}
		Collection<Point> tPoints = SideLocator.findPointsOfSector(sv.getPoints(), sv.getCenter(), border.fitSector());
		Iterator<? extends Point> pIter = tPoints.iterator();
		Ipoint first = border.getFirst();
		Ipoint second = border.getSecond();
		Point one = null, two = null, touch = null;
		float sumX = 0, sumY = 0;
		int cnt = 0;
		while(pIter.hasNext()){
			if(one == null){
				one = pIter.next();
			}
			two = pIter.next();
			touch = PointHelper.pointCross(one, two, first, second);
			one = two;
			if(touch != null){
				sumX += touch.getFX();
				sumY += touch.getFY();
				cnt++;
			}
		}
		if(cnt == 0){
			return null;
		}
		touch = new Fpoint(sumX / cnt, sumY / cnt);
		sv.occupy();
		return new BorderEvent(sv, border, touch);
	}
/*	
	private boolean checkIntHistory(Liver target, Boundary check){
		Event hEvent = target.getHistory(BaseEvent.touchEventCode);
		if(hEvent == null){
			return false;
		}
		return check.equals(hEvent.getEventSource());
	}
*/
}
