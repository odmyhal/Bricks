package org.bricks.engine.event.overlap;

import java.util.Collection;
import java.util.Iterator;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.pool.BrickSubject;

public class BrickOverlapAlgorithm extends BaseOverlapAlgorithm<EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, Point>{

	public static final BrickOverlapAlgorithm instance = new BrickOverlapAlgorithm();
	private static final int[] sectors = new int[5];
	static{
		sectors[0] = 0;
		sectors[1] = 3;
		sectors[2] = 4;
		sectors[3] = 1;
		sectors[4] = 2;
	}
/*	
	public OverlapEvent<EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, Point> checkOverlap(
			EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> target,
			EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> client) {
		return checkOverlapPoint(target, client);
	}
*/
	
	
	public Point findOverlapPoint(EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> one, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> two){
		return findOverlapPoint(one, two, true);
	}
	
	public boolean isOvarlap(EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> one, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> two){
		return findOverlapPoint(one, two, false) != null;
	}
/*
	@Override
	public OverlapEvent<EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, Point>
		checkOverlap(EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> targetView, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> checkView){
		Point overlapPoint = findOverlapPoint(targetView, checkView);
		if(overlapPoint == null){
			return null;
		}
		return new OverlapEvent<EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint>, Point>
				(targetView, checkView, overlapPoint);
	}
*/	
	public static final boolean isPointSetOverlap(PointSetPrint one, PointSetPrint two){
		return findOverlapPoint(one, two, false) != null;
	}

	private static final Point findOverlapPoint(PointSetPrint one, PointSetPrint two, boolean presize){
		
		Dimentions dimm1 = one.getDimentions();
		Dimentions dimm2 = two.getDimentions();
		if(dimm1.getMaxX() < dimm2.getMinX() || dimm1.getMinX() > dimm2.getMaxX()
				|| dimm1.getMaxY() < dimm2.getMinY() || dimm1.getMinY() > dimm2.getMaxY()){
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
			return resolveTouchPoint(oneTouch, twoTouch);
		}else{
			return null;
		}
	}

	private static final Point resolveTouchPoint(Point[] oneTouch, Point[] twoTouch){
		if(oneTouch[0] == null){
			return null;
		}
		if(twoTouch[0] == null){
			return oneTouch[0];
		}

		if(oneTouch[4] == twoTouch[3]){
			return oneTouch[4];
		}
		return new Fpoint((oneTouch[0].getFX() + twoTouch[0].getFX()) / 2, (oneTouch[0].getFY() + twoTouch[0].getFY()) / 2);
	}
}
