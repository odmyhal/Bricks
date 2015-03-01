package org.bricks.core.help;

import java.util.Iterator;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.exception.Validate;

public class ConvexityApproveHelper {
	
	private static final LocalPoint firstLocalVector = new LocalPoint();
	private static final LocalPoint secondLocalVector = new LocalPoint();

	public static void applyConvexity(PointSetPrint psv){
		applyConvexity(psv.getPoints());
	}
	
	public static void applyConvexity(Iterable<Ipoint> collection){
		Iterator<Ipoint> pIter = collection.iterator();
		Ipoint first = pIter.next();
		Ipoint second = pIter.next();
		Ipoint one = first, two = second, three = null;
		while(pIter.hasNext()){
			three = pIter.next();
			checkCorner(one, two, three);
			one = two;
			two = three;
		}
		checkCorner(one, two, first);
		checkCorner(two, first, second);
	}
	
	private static void checkCorner(Ipoint one, Ipoint two, Ipoint three){
		Validate.isTrue(isCornerConvex(one, two, three), String.format("Corner is not convex (%s, %s, %s)", one, two, three));
	}
	
	private static boolean isCornerConvex(Ipoint one, Ipoint two, Ipoint three){
		return cornerRad(one, two, three) >= Math.PI;
	}
	
	private static double cornerRad(Ipoint one, Ipoint two, Ipoint three){
		Fpoint firstVector = firstLocalVector.get();
		Fpoint secondVector = secondLocalVector.get();
		firstVector.set(one.getX() - two.getX(), one.getY() - two.getY());
		secondVector.set(three.getX() - two.getX(), three.getY() - two.getY());
		PointHelper.normalize(firstVector);
		PointHelper.normalize(secondVector);
		double firstRad = AlgebraHelper.trigToRadians(firstVector.getFX(), firstVector.getFY());
		double secondRad = AlgebraHelper.trigToRadians(secondVector.getFX(), secondVector.getFY());
		while(secondRad < firstRad){
			secondRad += Math.PI * 2;
		}
		return secondRad - firstRad;
	}
	
	
	private static class LocalPoint extends ThreadLocal<Fpoint>{
		@Override protected Fpoint initialValue() {
            return new Fpoint(0f, 0f);
        }
	}
}
