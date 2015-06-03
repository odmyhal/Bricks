package org.bricks.extent.engine.processor;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.staff.Walker;
import org.bricks.exception.Validate;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;
import com.badlogic.gdx.math.Vector3;
import org.bricks.engine.neve.WalkPrint;

public abstract class RollNodeToWalkerVProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, B extends Walker<WalkPrint<?, Fpoint>, ?>> 
	extends RollNodeToEntityVProcessor<T, B>{
	
	private static final double minRad = -Math.PI / 4, maxRad = Math.PI / 4, minLenDiff = 1;
//	private boolean accessible = false;
	private Fpoint accessiblePoint = new Fpoint();

	public RollNodeToWalkerVProcessor(T target, String nodeOperatorName) {
		super(target, nodeOperatorName);
	}
/*	
	public abstract Vector3 provideStartPoint(T target, long processTime);
	public abstract void fetchButtPoint(B butt, Vector3 dest);
	public abstract float convertToTargetRotation(double rad);
*/
	public abstract void fetchButtPoint(WalkPrint<?, Fpoint> buttPrint, Vector3 dest);
	
	@Override
	public void doJob(T target, long processTime) {
		approve = false;
		WalkPrint<?, Fpoint> buttPrint = butt.getSafePrint();
		fetchButtPoint(buttPrint, this.buttCenter);
		Fpoint buttVector = buttPrint.getVector().source;
		Vector3 myCenter = provideStartPoint(target, processTime);
		
		double maxAngleDiff = tryAngle(myCenter, buttCenter, buttVector, maxRad);
		double minAngleDiff = tryAngle(myCenter, buttCenter, buttVector, minRad);
		if(maxAngleDiff < -minLenDiff
				|| minAngleDiff > minLenDiff){
			System.out.println("wrong destination maxAngleDiff " + maxAngleDiff + " minAngleDiff " + minAngleDiff);
			accessiblePoint.x = buttCenter.x;
			accessiblePoint.y = buttCenter.y;
			lastCheckTime = processTime;
			buttPrint.free();
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
//		System.out.println("Calculated rotation: " + curRad);
		float targetRotation = convertToTargetRotation(curRad);
//		System.out.println("Target rotation:    " + targetRotation);
		rotateToTarget(targetRotation, processTime);
		buttPrint.free();
		approve = true;
	}
	
	private double tryAngle(Vector3 myCenter, Vector3 buttCenter, Fpoint buttVector, double angleRad){

		double vSpeed = bulletSpeed * Math.sin(angleRad);
//		System.out.println("Vertical speed " + vSpeed);
		double startVPosition = myCenter.z;
		double startVSpeed = vSpeed;
		double t = 0;
		if(vSpeed > 0){
			t += vSpeed / Math.abs(bulletAcceleration);
			startVPosition += t * vSpeed / 2;
			startVSpeed = 0;
		}
		if(startVPosition < buttCenter.z){
			return Double.MIN_VALUE;
		}
//		System.out.println("interval time: " + t);
//		System.out.println("StartVPosition: " + startVPosition + ", buttCenter: " + buttCenter.z);
		double b = 2 * startVSpeed;
//		double c = -2 * (startVPosition - buttCenter.z);
		double d = AlgebraHelper.pow(b, 2) + 8 * bulletAcceleration * (buttCenter.z - startVPosition);
		Validate.isFalse(d < 0);
		double ds = Math.sqrt(d);
		double x1 = (-b + ds) / Math.scalb(bulletAcceleration, 1);
		double x2 = (-b - ds) / Math.scalb(bulletAcceleration, 1);
		Validate.isFalse(x1 * x2 > 0, "Result must have differ sign");
		t += Math.max(x1, x2);
//		System.out.println("Total time: " + t);
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
	
	public Fpoint accessiblePoint(){
		return accessiblePoint;
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}

}
