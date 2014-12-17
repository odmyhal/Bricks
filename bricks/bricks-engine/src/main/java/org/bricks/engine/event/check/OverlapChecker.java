package org.bricks.engine.event.check;

import java.util.Collection;
import java.util.Iterator;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.EventTarget;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.pool.Area;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.view.PointSetView;
import org.bricks.engine.view.SubjectView;

public class OverlapChecker extends EventChecker{
	
	private static final int[] sectors = new int[5];
	private static final OverlapChecker instance = new OverlapChecker();
	
	static{
		sectors[0] = 0;
		sectors[1] = 3;
		sectors[2] = 4;
		sectors[3] = 1;
		sectors[4] = 2;
	}
	

	private final ThreadLocal<EventCheckState> localCheckState = new ThreadLocal<EventCheckState>() {
        @Override protected EventCheckState initialValue() {
            return new EventCheckState();
        }
    };
	
	
	private OverlapChecker(){};
	
	public static OverlapChecker instance(){
		return instance;
	}
	
	protected Event popEvent(Liver targetP){
		EventCheckState curState = localCheckState.get();
		if(curState.checkNew(targetP)){
			Subject last = checkLast(targetP);
			curState.setLast(last);
		}
		int entityNum = curState.getEntityState();
		while(entityNum < targetP.getStaff().size()){
			Subject target = (Subject) targetP.getStaff().get(entityNum);
//			Validate.isTrue(target != null, "Subject " + entityNum + " of " + targetP.getClass().getSimpleName() + " is null");
//			Validate.isTrue(target.getDistrict() != null, "Subject " + entityNum + " of " + targetP.getClass().getSimpleName() + "(" + "Subject: " + target + ", entity: " + targetP + ")" + " has not district");
//			Validate.isTrue(target.getDistrict().getBuffer() != null, "Subject " + entityNum + " of " + targetP.getClass().getSimpleName() + " district buffer is null");
			Area area = target.getDistrict().getBuffer();
			int areaNum = curState.getAndIncrementAreaState();
			while(areaNum < area.capacity()){
				Subject<?> sv = area.getSubject(areaNum);
				areaNum = curState.getAndIncrementAreaState();
				if(sv == null || sv == curState.getLast() || sv == target){
					continue;
				}
				if(!targetP.needCheckOverlap(sv)){
					continue;
				}
				//Synchronize with EventChecker.checkEvents()
				synchronized(sv.getEntity()){
					SubjectView targetView = target.getCurrentView();
					SubjectView checkView = sv.getOccupiedCurrentView();
					OverlapEvent oe = checkOverlap(targetView, checkView);
					if(oe != null){
						targetView.occupy();
						targetP.putHistory(oe);
						if(sv.getEntity().isEventTarget()){
							EventTarget svt = (EventTarget) sv.getEntity();
							if(svt.checkerRegistered(this)){
								OverlapEvent svEvent = new OverlapEvent(checkView, targetView, oe.getTouchPoint(), oe.getCrashNumber());
								svt.addEvent(svEvent);
								svt.putHistory(svEvent);
							}
						}
						return oe;
					}
					checkView.free();
				}
			}
			entityNum = curState.incrementEntityState();
		}
		curState.reject();
		return null;
	}
	
	private Subject checkLast(Liver target){
		Event hEvent = target.getHistory(BaseEvent.touchEventCode);
		if(hEvent == null || !(hEvent instanceof OverlapEvent)){
			return null;
		}
		OverlapEvent oEvent = (OverlapEvent) hEvent;
		SubjectView sv = oEvent.getSourceView();
		Subject<?> check = sv.getSubject();
		if(!target.needCheckOverlap(check)){
			return null;
		}
		if(check.getEntity().isEventTarget()){
			EventTarget checkT = (EventTarget) check.getEntity();
			Event lastCheckEvent = checkT.getHistory(BaseEvent.touchEventCode);
			if(lastCheckEvent == null || !(lastCheckEvent instanceof OverlapEvent)){
				target.removeHistory(BaseEvent.touchEventCode);
				return null;
			}
			if( ((OverlapEvent) lastCheckEvent).getCrashNumber() != oEvent.getCrashNumber() ){
				target.removeHistory(BaseEvent.touchEventCode);
				return null;
			}
		}
		Subject<?> ts = oEvent.getTargetView().getSubject();
		Validate.isTrue(target.equals(ts.getEntity()));
		SubjectView checkView = check.getOccupiedCurrentView();
		Point touchPoint = findOverlapPoint(ts.getCurrentView(), checkView);
		if(touchPoint == null){
			target.removeHistory(BaseEvent.touchEventCode);
			return null;
		}
		checkView.free();
		return check;
	}
	
	private OverlapEvent checkOverlap(SubjectView targetView, SubjectView checkView){
		Point overlapPoint = findOverlapPoint(targetView, checkView);
		if(overlapPoint == null){
			return null;
		}
		return new OverlapEvent(targetView, checkView, overlapPoint);
	}

	private static Point findOverlapPoint(PointSetView one, PointSetView two){
		return findOverlapPoint(one, two, true);
	}
	
	public static boolean isOvarlap(PointSetView one, PointSetView two){
		return findOverlapPoint(one, two, false) != null;
	}
	
	private static Point findOverlapPoint(PointSetView one, PointSetView two, boolean presize){
/*		
		Liver target = null;
		if(one instanceof SubjectView){
			System.out.println("TArget Not nulL");
			if(one.getPoints().size() == 4){
				System.out.println("Checking my point");
				Entity e = ((SubjectView) one).getSubject().getEntity();
				if(e instanceof Liver){
					target = (Liver) e;
				}
			}else{
				System.out.println("Chekcin entity of size " + one.getPoints().size());
			}
		}
*/		
		Dimentions dimm1 = one.getDimentions();
		Dimentions dimm2 = two.getDimentions();
		if(dimm1.getMaxX() < dimm2.getMinX() || dimm1.getMinX() > dimm2.getMaxX()
				|| dimm1.getMaxY() < dimm2.getMinY() || dimm1.getMinY() > dimm2.getMaxY()){
/*			if(target != null){
				target.startLog();
				target.appendLog("Diff dimentions");
				target.appendLog("One: " + one.getPoints());
				target.appendLog("Two: " + one.getPoints());
				target.finishLog();
			}*/
			return null;
		}
		int oneSector = PointHelper.detectSectorByPoint(one.getCenter(), two.getCenter());
		int twoSector = sectors[oneSector];
		
		Collection<Point> onePoints = one.getPointsOfSector(oneSector);
		Collection<Point> twoPoints = two.getPointsOfSector(twoSector);
		
		Point oneCenter = one.getCenter();
		Point twoCenter = two.getCenter();
		Point oneCross = null, firstCheck = null, secondCheck;
		Iterator<Point> oneIter = onePoints.iterator(); 
		while(oneCross == null && oneIter.hasNext()){
			if(firstCheck == null){
				firstCheck = oneIter.next();
			}
			secondCheck = oneIter.next();
			oneCross = PointHelper.pointCross(firstCheck, secondCheck, oneCenter, twoCenter);
			firstCheck = secondCheck;
		}
		if(oneCross == null){
			return new Fpoint((oneCenter.getFX() + twoCenter.getFX()) / 2, (oneCenter.getFY() + twoCenter.getFY()) / 2);
		}
		firstCheck = null;
		secondCheck = null;
		Point twoCross = null;
		Iterator<Point> twoIter = twoPoints.iterator();
		while(twoCross == null && twoIter.hasNext()){
			if(firstCheck == null){
				firstCheck = twoIter.next();
			}
			secondCheck = twoIter.next();
			twoCross = PointHelper.pointCross(firstCheck, secondCheck, oneCenter, twoCenter);
			firstCheck = secondCheck;
		}
		if(twoCross == null){
			return new Fpoint((oneCenter.getFX() + twoCenter.getFX()) / 2, (oneCenter.getFY() + twoCenter.getFY()) / 2);
		}
		if(twoCross.getFX() >= Math.min(oneCenter.getFX(), oneCross.getFX())
				&& twoCross.getFX() <= Math.max(oneCenter.getFX(), oneCross.getFX())
				&& twoCross.getFY() >= Math.min(oneCenter.getFY(), oneCross.getFY())
				&& twoCross.getFY() <= Math.max(oneCenter.getFY(), oneCross.getFY())
						){
			return new Fpoint((oneCenter.getFX() + twoCenter.getFX()) / 2, (oneCenter.getFY() + twoCenter.getFY()) / 2);
		}
		oneIter = onePoints.iterator();
		Point firstOne = null, secondOne, firstTwo = null, secondTwo;
		Point[] oneTouch = null, twoTouch = null;
		if(presize){
			oneTouch = new Point[5];
			twoTouch = new Point[5];
		}
		lookup: while(oneIter.hasNext()){
			if(firstOne == null){
				firstOne = oneIter.next();
			}
			secondOne = oneIter.next();
			twoIter = twoPoints.iterator();
			while(twoIter.hasNext()){
				if(firstTwo == null){
					firstTwo = twoIter.next();
				}
				secondTwo = twoIter.next();
				Point res = PointHelper.pointCross(firstOne, secondOne, firstTwo, secondTwo);
				if(res != null){
					if(presize){
						if(oneTouch[0] == null){
							oneTouch[0] = res;
							oneTouch[1] = firstOne;
							oneTouch[2] = secondOne;
							oneTouch[3] = firstTwo;
							oneTouch[4] = secondTwo;
						}else if(twoTouch[0] == null){
							twoTouch[0] = res;
							twoTouch[1] = firstOne;
							twoTouch[2] = secondOne;
							twoTouch[3] = firstTwo;
							twoTouch[4] = secondTwo;
							break lookup;
						}
					}else{
						return res;
					}
				}
				firstTwo = secondTwo;
			}
			firstOne = secondOne;
		}
		if(presize){
/*			System.out.println("Finished priesice check");
			if(target != null){
				target.startLog();
				if(oneTouch[0] == null){
					target.appendLog("Has not found overlap");
				}else{
					target.appendLog("Has Found overlap");
				}
				target.appendLog("One: " + one.getPoints());
				target.appendLog("Two: " + one.getPoints());
				target.finishLog();
			}*/
			return resolveTouchPoint(oneTouch, twoTouch);
		}else{
			return null;
		}
	}

	private static Point resolveTouchPoint(Point[] oneTouch, Point[] twoTouch){
		if(oneTouch[0] == null){
			return null;
		}
		if(twoTouch[0] == null){
			return oneTouch[0];
		}
/*		if(oneTouch[1] == twoTouch[1] || oneTouch[3] == twoTouch[3]){
			return new Fpoint((oneTouch[0].getFX() + twoTouch[0].getFX()) / 2, (oneTouch[0].getFY() + twoTouch[0].getFY()) / 2);
		}*/
		if(oneTouch[4] == twoTouch[3]){
			return oneTouch[4];
		}
		return new Fpoint((oneTouch[0].getFX() + twoTouch[0].getFX()) / 2, (oneTouch[0].getFY() + twoTouch[0].getFY()) / 2);
	}
}
