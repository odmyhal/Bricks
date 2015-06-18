package org.bricks.extent.processor.tbroll;

import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Tuple;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.staff.Walker;
import org.bricks.exception.Validate;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;
import com.badlogic.gdx.math.Vector3;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.utils.Cache;

public abstract class RollNodeToWalkerVProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>> 
	extends RollNodeToEntityVProcessor<T>{
	
	private static final double minRad = -Math.PI / 4, maxRad = Math.PI / 4, minLenDiff = 1;
//	private boolean accessible = false;
	private Fpoint accessiblePoint = Cache.get(Fpoint.class);
	private Vector3 buttVector = Cache.get(Vector3.class);
	private static final long idleMinTime = 300L;
	
	private static final int buttHistorySize = 5;
	private LinkedList<Tuple<Long, Vector3>> buttHistory = new LinkedList<Tuple<Long, Vector3>>();
	

	public RollNodeToWalkerVProcessor(T target, String nodeOperatorName) {
		super(target, nodeOperatorName);
	}

//	public abstract void fetchButtPoint(WalkPrint<?, Fpoint> buttPrint, Vector3 dest);
	
	//Should be called in motor thread
	public void setButt(Butt butt){
		while(!buttHistory.isEmpty()){
			Tuple<Long, Vector3> tpl = buttHistory.poll();
			Cache.put(tpl.getSecond());
			Cache.put(tpl);
		}
		buttVector.set(0f, 0f, 0f);
		this.butt = butt;
	}
	
	@Override
	public void doJob(T target, long processTime) {
		approve = false;
//		WalkPrint<?, Fpoint> buttPrint = butt.getSafePrint();
//		fetchButtPoint(buttPrint, this.buttCenter);
//		Fpoint buttVector = buttPrint.getVector().source;
		butt.fetchOrigin(buttCenter);
		calculateButtVector(buttCenter, processTime);
		Vector3 myCenter = provideStartPoint(target, processTime);
		
		double maxAngleDiff = tryAngle(myCenter, buttCenter, buttVector, maxRad);
		double minAngleDiff = tryAngle(myCenter, buttCenter, buttVector, minRad);
		if(maxAngleDiff < -minLenDiff
				|| minAngleDiff > minLenDiff){
			System.out.println("wrong destination maxAngleDiff " + maxAngleDiff + " minAngleDiff " + minAngleDiff);
			accessiblePoint.x = buttCenter.x;
			accessiblePoint.y = buttCenter.y;
			lastCheckTime = processTime;
//			buttPrint.free();
			return;
		}
//		accessible = true;
		double step = (maxRad - minRad);
		double curRad = maxRad;
		double diff = Double.MAX_VALUE;
		int counter = 0;
		while(Math.abs(diff) > minLenDiff){
			step /= 2;
			if(diff > 0){
				curRad -= step;
			}else{
				curRad += step;
			}
			diff = tryAngle(myCenter, buttCenter, buttVector, curRad);
			Validate.isTrue(++counter < 100, "Something is wrong here...");
		};
		float targetRotation = convertToTargetRotation(curRad);
		rotateToTarget(targetRotation, processTime);
		approve = true;
	}

	private double tryAngle(Vector3 myCenter, Vector3 buttCenter, Vector3 buttVector, double angleRad){
//		double a = bulletAcceleration / 2;
		double b = bulletSpeed * Math.sin(angleRad) - buttVector.z;
//		double c = myCenter.z - buttCenter.z;
		double d = AlgebraHelper.pow(b, 2) - 2 * bulletAcceleration * (myCenter.z - buttCenter.z);
		if(d < 0){
			return Double.MIN_VALUE;
		}
		double ds = Math.sqrt(d);
		double x1 = (-b + ds) / bulletAcceleration;
		double x2 = (-b - ds) / bulletAcceleration;
		Validate.isFalse(x1 * x2 > 0, "Result must have differ sign");
		double t = Math.max(x1, x2);
		//define already time
		
		double xLen = buttCenter.x - myCenter.x + buttVector.x * t;
		double yLen = buttCenter.y - myCenter.y + buttVector.y * t;
		double tLen = Math.sqrt(AlgebraHelper.pow(xLen, 2) + AlgebraHelper.pow(yLen, 2));
		
		double hSpeed = bulletSpeed * Math.cos(angleRad);
		
		double res = hSpeed * t - tLen;
		if(Math.abs(res) < minLenDiff){
			accessiblePoint.x = (float) (buttCenter.x + buttVector.x * t);
			accessiblePoint.y = (float) (buttCenter.y + buttVector.y * t);
		}
		
		return res;
	}
	public void calculateButtVector(Vector3 buttOrigin, long curTime){
		
		Tuple<Long, Vector3> lastButtTuple = buttHistory.peekLast();
		if(lastButtTuple == null){
			Vector3 newButt = Cache.get(Vector3.class);
			newButt.set(buttOrigin);
			lastButtTuple = Cache.get(Tuple.class);
			lastButtTuple.setFirt(curTime);
			lastButtTuple.setSecond(newButt);
			buttHistory.addLast(lastButtTuple);
			return;
		}
		Vector3 lastButt = lastButtTuple.getSecond();
		if(lastButt.x == buttOrigin.x
				&& lastButt.y == buttOrigin.y
				&& lastButt.z == buttOrigin.z
				&& lastButtTuple.getFirst() + idleMinTime > curTime){
			return;
		}
		lastButt = Cache.get(Vector3.class);
		lastButt.set(buttOrigin);
		lastButtTuple = Cache.get(Tuple.class);
		lastButtTuple.setFirt(curTime);
		lastButtTuple.setSecond(lastButt);
		buttHistory.addLast(lastButtTuple);
		Tuple<Long, Vector3> firstButtTuple;
		boolean cacheBack = false;
		if(buttHistory.size() > buttHistorySize){
			firstButtTuple = buttHistory.pollFirst();
			cacheBack = true;
		}else{
			firstButtTuple = buttHistory.peekFirst();
		}
		float diffK = ((float)(curTime - firstButtTuple.getFirst())) / 1000f;
		Vector3 firstButt = firstButtTuple.getSecond();
		buttVector.x = (lastButt.x - firstButt.x) / diffK;
		buttVector.y = (lastButt.y - firstButt.y) / diffK;
		buttVector.z = (lastButt.z - firstButt.z) / diffK;
		if(cacheBack){
			Cache.put(firstButt);
			Cache.put(firstButtTuple);
		}
	}
	
	public Fpoint accessiblePoint(){
		return accessiblePoint;
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}

}
