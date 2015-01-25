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
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.view.SubjectView;

public class BorderTouchChecker extends EventChecker{
	
	private static final BorderTouchChecker instance = new BorderTouchChecker();
	
	private final ThreadLocal<EventCheckState> localCheckState = new ThreadLocal<EventCheckState>() {
        @Override protected EventCheckState initialValue() {
            return new EventCheckState();
        }
    };

	private BorderTouchChecker(){};
	
	public static BorderTouchChecker instance(){
		return instance;
	}

	@Override
	protected Event popEvent(Liver targetP, long eventTime) {
		EventCheckState eventState = localCheckState.get();
		eventState.checkNew(targetP);
		int entityNum = eventState.getEntityState();
		while(entityNum < targetP.getStaff().size()){
			Subject target = (Subject) targetP.getStaff().get(entityNum);
			bloop: while(true){
				int borderNum = eventState.getAndIncrementAreaState();
				Boundary border = target.getDistrict().getBoundary(borderNum);
				if(border == null){
					break bloop;
				}
//				targetP.startLog();
//				targetP.appendLog("With center " + target.getPoint() + " checking border: " + border);
//				targetP.appendLog("Current vector: " + ((Walker)targetP).getVector());
				BorderEvent event = checkTouch(target, border);
				boolean isLast = checkIntHistory(targetP, border);
				if(event == null){
//					targetP.appendLog("No touch found");
					if(isLast){
						targetP.removeHistory(BaseEvent.touchEventCode);
					}
				}else if(!isLast){
//					targetP.appendLog("Found touch: " + event.getTargetView());
//					targetP.finishLog();
					targetP.putHistory(event);
					return event;
				}
//				targetP.finishLog();
			}
			entityNum = eventState.incrementEntityState();
		}
		eventState.reject();
		return null;
	}
	
	private BorderEvent checkTouch(Subject target, Boundary border){
		SubjectView sv = target.getCurrentView();
		if(sv == null){
			return null;
		}
//		Collection<? extends Point> tPoints = sv.getPoints();
		Collection<Point> tPoints = SideLocator.findPointsOfSector(sv.getPoints(), sv.getCenter(), border.fitSector());
//		Collection<Point> tPoints = sv.getPointsOfSector(border.fitSector());
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
	
	private boolean checkIntHistory(Liver target, Boundary check){
		Event hEvent = target.getHistory(BaseEvent.touchEventCode);
		if(hEvent == null){
			return false;
		}
		return check.equals(hEvent.getEventSource());
	}

}
