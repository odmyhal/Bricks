package org.bricks.extent.engine.processor;

import org.bricks.core.help.AlgebraHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.staff.Entity;
import org.bricks.extent.space.overlap.MarkPoint;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public abstract class RollNodeToEntityVProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, C>
extends NodeModifyProcessor<T> {
	
	private Entity<EntityPrint<?, C>> butt;
//	private MarkPoint rollPointMark;
	
	private float rotationSpeed, curRotationSpeed, bulletSpeed, bulletAcceleration;
	private Vector3 buttCenter = new Vector3();

	public RollNodeToEntityVProcessor(T target, String nodeOperatorName/*, Matrix4... linkMatrices*/) {
		super(target, nodeOperatorName);
/*		Vector3 rollCenter = new Vector3(this.nodeOperator.linkPoint());
		Matrix4 invMatrix = new Matrix4(linkMatrices[linkMatrices.length - 1]);
		invMatrix.inv();
		rollCenter.mul(invMatrix);
		System.out.println("ADDED rotationCenter: " + rollCenter);
		rollPointMark = new MarkPoint(rollCenter);
		for(Matrix4 matrix: linkMatrices){
			rollPointMark.addTransform(matrix);
		}
		rollPointMark.calculateTransforms();*/
//		System.out.println("FOUND center: " + rollPointMark.getMark(0));
	}
	
	public void setButt(Entity butt){
		this.butt = butt;
	}
	
	public void setRotationSpeed(float rs){
		rotationSpeed = rs;
	}
	
	public void setBulletSpeed(float bs){
		bulletSpeed = bs;
	}
	
	public void setBulletAcceleration(float ba){
		bulletAcceleration = ba;
	}
	
	public abstract Vector3 provideStartPoint(T target, long processTime);
	public abstract void fetchButtPoint(C buttOrigin, Vector3 dest);
	public abstract float convertToTargetRotation(double rad);

//	int log = 0;
	@Override
	public void doJob(T target, long processTime) {
		EntityPrint<?, C> buttPrint = butt.getSafePrint();
		fetchButtPoint(buttPrint.getOrigin().source, buttCenter);
		buttPrint.free();
		
//		fetchButtPoint(butt, buttCenter);
		
//		rollPointMark.calculateTransforms();
//		Vector3 myCenter = rollPointMark.getMark(0);
		Vector3 myCenter = provideStartPoint(target, processTime);
		
		double sf = 0;//myCenter.z - buttCenter.z;
		double s2 = AlgebraHelper.pow(bulletSpeed, 2);
		double l2 = AlgebraHelper.pow(Math.hypot(myCenter.x - buttCenter.x, myCenter.y - buttCenter.y), 2);
		float acc = Math.abs(bulletAcceleration);
		
		double a = Math.scalb( AlgebraHelper.pow(s2, 2) * (AlgebraHelper.pow(sf, 2) + l2) , 2);
		if( a == 0 ){
			lastCheckTime = processTime;
			return;
		}
		double c = AlgebraHelper.pow(l2, 2) * AlgebraHelper.pow(acc, 2);
		if( c == 0 ){
			lastCheckTime = processTime;
			return;
		}
		double b = Math.scalb( -s2 * l2 * (sf * acc + s2), 2);
//		double d = AlgebraHelper.pow(b, 2) - Math.scalb(a * c, 2);
		
		double d = AlgebraHelper.pow(b, 2) - 4 * a * c;
/*		if(++log > 30000){
//			System.out.format("a = %.2f, b = %.2f, c = %.2f, d = %.2f\n", a, b, c, d);
			System.out.println("My center " + myCenter + ", target " + buttCenter);
//			System.out.println("S2 = " + s2 + ", l2 = " + l2 + ", sf = " + sf);
			log = 0;
		}*/
		if(d < 0){
/*			if(log > 30000){
				System.out.println("Bad discriminant");
				log = 0;
			}*/
			lastCheckTime = processTime;
			return;
		}
		double ds = Math.sqrt(d);
		double x1 = (-b + ds) / Math.scalb(a, 1);
		double x2 = (-b - ds) / Math.scalb(a, 1);
		double x;
		if(x1 >= 0 || x2 >= 0){
			x = Math.max(x1, x2);
		}else if(x1 >= 0){
			x = x1;
		}else if(x2 >= 0){
			x = x2;
		}else{
			lastCheckTime = processTime;
/*			if(log > 30000){
				System.out.println("Bad both results");
				log = 0;
			}*/
			return;
		}
/*		if(log > 30000){
			System.out.println("Found results: " + Math.acos(Math.sqrt(x)));
		}*/
		float targetRotation = convertToTargetRotation(Math.acos(Math.sqrt(x)));
		curRotationSpeed = rotationSpeed;
		float diff = targetRotation - nodeOperator.rotatedRadians();
		if(Math.abs(diff) > minDiff){
//			System.out.println("Rotated VProcessor to " + diff + " radians");
			lastCheckTime = processTime;
			nodeOperator.rotate(diff);
			nodeOperator.updatePrint();
			subject.adjustCurrentPrint();
		}
/*		if(Math.abs(diff) < minDiff){
			lastCheckTime = processTime;
			return;
		}
		if(diff < 0){
			curRotationSpeed *= -1;
		}
		float diffTime = processTime - lastCheckTime;
		float rRad = diffTime * curRotationSpeed / 1000f;
		if(Math.abs(rRad) > minDiff){
			lastCheckTime = processTime;
			nodeOperator.rotate(rRad);
			nodeOperator.updatePrint();
			subject.adjustCurrentPrint();
		}*/
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}

}
