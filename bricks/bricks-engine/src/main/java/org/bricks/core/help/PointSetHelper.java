package org.bricks.core.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.Fpoint;

public class PointSetHelper {
	
	private static final float presize = 1f;

	
	public static Fpoint detectCentralPoint(Collection<? extends Point> points){
		
		Dimentions dimm = fetchDimentions(points);
		Fpoint centralPoint = new Fpoint((dimm.getMaxX() + dimm.getMinX()) / 2, (dimm.getMaxY() + dimm.getMinY()) / 2);
		points = calibrateByCenter(centralPoint, points);
		Point presizeDiff = evalPresizeSquareDiff(centralPoint, points);
		
		int count = 1;
		while(Math.abs(presizeDiff.getFX()) > presize || Math.abs(presizeDiff.getFY()) > presize){
			centralPoint.translate(presizeDiff.getFX() / 2, presizeDiff.getFY() / 2);
			points = calibrateByCenter(centralPoint, points);
			presizeDiff = evalPresizeSquareDiff(centralPoint, points);
			Validate.isTrue(++count < 30, "Too many loops for central point search");
		}
		
		return centralPoint;
	}
	
	private static Collection<Point> calibrateByCenter(Point newCenter, Collection<? extends Point> points){
		List<Point> result = new ArrayList<Point>(points.size());
		
		Iterator<? extends Point> pIter = points.iterator();
		boolean add = false;
		int sector = 0;
		while(true){
			if(!pIter.hasNext()){
				pIter = points.iterator();
			}
			Point curP = pIter.next();
			int curSector = PointHelper.detectSectorByPoint(newCenter, curP);
			if(curSector < sector){
				if(add){
					break;
				}else{
					add = true;
				}
			}
			sector = curSector;
			
			if(add){
				result.add(curP);
			}
		}
		Validate.isTrue(result.size() == points.size(), "Wrong points number in calibration results");
		return result;
	}
	
	private static Point evalPresizeSquareDiff(Point tryCentralPoint, Collection<? extends Point> points){
		Dimentions normalCross = calcNormalCrossOfPoint(tryCentralPoint, points);
		LinkedList<Point> smallX = new LinkedList<Point>();
		LinkedList<Point> hugeX = new LinkedList<Point>();
		LinkedList<Point> smallY = new LinkedList<Point>();
		LinkedList<Point> hugeY = new LinkedList<Point>();
		hugeY.add(new Fpoint(normalCross.getMaxX(), tryCentralPoint.getFY()));
		smallY.add(new Fpoint(normalCross.getMinX(), tryCentralPoint.getFY()));
		hugeX.add(new Fpoint(tryCentralPoint.getFX(), normalCross.getMinY()));
		smallX.add(new Fpoint(tryCentralPoint.getFX(), normalCross.getMaxY()));
		
		Iterator<? extends Point> pIter = points.iterator();
		int cycle = 0;
		LinkedList<Point> curXList = null;
		LinkedList<Point> curYList = null;
		int sector = 0;
		while(true){
			if(!pIter.hasNext()){
				pIter = points.iterator();
			}
			Point curP = pIter.next();
			int curSector = PointHelper.detectSectorByPoint(tryCentralPoint, curP);
			if(curSector > sector){
				if(cycle > 0){
					break;
				}
				if(curSector == 1){
					curYList = hugeY;
					curXList = null;
				}else if(curSector == 2){
					curXList = smallX;
					curYList = hugeY;
				}else if(curSector == 3){
					curYList = smallY;
					curXList = smallX;
				}else if(curSector == 4){
					curXList = hugeX;
					curYList = smallY;
				}
			}else if(curSector < sector){
				if(++cycle > 1){
					break;
				}
				curYList = null;
				if(curSector == 1){
					curXList = hugeX;
				}else{
					break;
				}
			}
			sector = curSector;
			
			if(curXList != null){
				curXList.add(curP);
			}
			if(curYList != null){
				curYList.add(curP);
			}
		}
		hugeY.add(new Fpoint(normalCross.getMinX(), tryCentralPoint.getFY()));
		smallY.add(new Fpoint(normalCross.getMaxX(), tryCentralPoint.getFY()));
		hugeX.add(new Fpoint(tryCentralPoint.getFX(), normalCross.getMaxY()));
		smallX.add(new Fpoint(tryCentralPoint.getFX(), normalCross.getMinY()));
		
		float hugeYArea = calcTrianglesArea(tryCentralPoint, hugeY);
		float smallYArea = calcTrianglesArea(tryCentralPoint, smallY);
		float hugeXArea = calcTrianglesArea(tryCentralPoint, hugeX);
		float smallXArea = calcTrianglesArea(tryCentralPoint, smallX);
		
		float difY = (hugeYArea - smallYArea) / (normalCross.getMaxX() - normalCross.getMinX());
		float difX = (hugeXArea - smallXArea) / (normalCross.getMaxY() - normalCross.getMinY());
		
		return new Fpoint(difX, difY);
	}
	
	private static float calcTrianglesArea(Point centralPoint, Collection<Point> points){
		float result = 0f;
		Point one = null, two = null;
		Iterator<Point> pIter = points.iterator();
		while(pIter.hasNext()){
			if(one == null){
				one = pIter.next();
			}else{
				one = two;
			}
			two = pIter.next();
			result += AlgebraHelper.triangleArea(one, two, centralPoint);
		}
		
		return result;
	}
	
	private static Dimentions calcNormalCrossOfPoint(Point central, Collection<? extends Point> points){
		Dimentions dimmY = calcCrossY(central.getFX(), points);
		Dimentions dimmX = calcCrossX(central.getFY(), points);
		dimmX.setMinYPoint(dimmY.getMinYPoint());
		dimmX.setMaxYPoint(dimmY.getMaxYPoint());
		return dimmX;
	}
	
	private static Dimentions calcCrossY(float curX, Collection<? extends Point> points){
		Validate.notEmpty(points, "Empty points of calibration");
		Validate.isTrue(points.size() > 2, "We need at least three points");
		List<Float> prepared = new ArrayList<Float>(2);
		Dimentions result = new Dimentions();
		Iterator<? extends Point> pIter = points.iterator();
		Point one = null, two = null, first = null;
		boolean cont = true;
		while(cont){
			if(one == null){
				one = pIter.next();
				first = one;
			}
			if(pIter.hasNext()){
				two = pIter.next();
			}else{
				two = first;
				cont = false;
			}
			if(curX > Math.min(one.getFX(), two.getFX())
					&& curX <= Math.max(one.getFX(), two.getFX())){
				if(one.getFX() == two.getFX()){
					prepared.add(one.getFX());
				}else{
					prepared.add(PointHelper.calcYOnLine(one, two, curX));
				}
			}
			one = two;
		}
		Validate.isTrue(prepared.size() == 2, "Expected two cross points");
		result.setMinYPoint(new Fpoint(0, Math.min(prepared.get(0), prepared.get(1))));
		result.setMaxYPoint(new Fpoint(0, Math.max(prepared.get(0), prepared.get(1))));
		return result;
	}
	
	private static Dimentions calcCrossX(float curY, Collection<? extends Point> points){
		Validate.notEmpty(points, "Empty points of calibration");
		Validate.isTrue(points.size() > 2, "We need at least three points");
		List<Float> prepared = new ArrayList<Float>(2);
		Dimentions result = new Dimentions();
		Iterator<? extends Point> pIter = points.iterator();
		Point one = null, two = null, first = null;
		boolean cont = true;
		while(cont){
			if(one == null){
				one = pIter.next();
				first = one;
			}
			if(pIter.hasNext()){
				two = pIter.next();
			}else{
				two = first;
				cont = false;
			}
			if(curY > Math.min(one.getFY(), two.getFY())
					&& curY <= Math.max(one.getFY(), two.getFY())){
				if(one.getFX() == two.getFX()){
					prepared.add(one.getFX());
				}else{
					prepared.add(PointHelper.calcXOnLine(one, two, curY));
				}
			}
			one = two;
		}
		Validate.isTrue(prepared.size() == 2, "Expected two cross points");
		result.setMinXPoint(new Fpoint(Math.min(prepared.get(0), prepared.get(1)), 0));
		result.setMaxXPoint(new Fpoint(Math.max(prepared.get(0), prepared.get(1)), 0));
		return result;
	}
	
	public static Dimentions fetchDimentions(Collection<? extends Point> points){
		
		Dimentions result = new Dimentions();
		
		for(Point point : points){
			if(result.getMinX() > point.getFX()){
				result.setMinXPoint(point);
			}
			if(result.getMaxX() < point.getFX()){
				result.setMaxXPoint(point);
			}
			if(result.getMinY() > point.getFY()){
				result.setMinYPoint(point);
			}
			if(result.getMaxY() < point.getFY()){
				result.setMaxYPoint(point);
			}
		}
		return result;
	}
	
	public static Collection<Point> getPointsOfSector(Collection<Point> points, Dimentions dimm, Point center, int sectorNum){
		Collection<Point> res = new ArrayList<Point>();
		Iterator<Point> pIter = points.iterator();
		boolean started = false;
		Point previous = pIter.next();
		
		
		return res;
	}

}
