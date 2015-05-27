package org.bricks.extent.engine.processor;

import org.bricks.core.help.AlgebraHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.processor.tool.Approver;
import org.bricks.engine.staff.Entity;
import org.bricks.extent.space.overlap.MarkPoint;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * This Approver is not thread safe, so can be used only by ApproveProcessor added to the same liver (run in the same motor thread)
 * @author oleh
 *
 * @param <T>
 * @param <C>
 */
public abstract class RollNodeToEntityVProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, B extends Entity<?>>
	extends NodeModifyProcessor<T> 
	implements Approver<T>{
	
	private B butt;
	private boolean approve = false;
//	private MarkPoint rollPointMark;
	
	private float rotationSpeed, /*curRotationSpeed,*/ bulletSpeed, bulletAcceleration;
	private Vector3 buttCenter = new Vector3();

	public RollNodeToEntityVProcessor(T target, String nodeOperatorName) {
		super(target, nodeOperatorName);
	}
	
	public void setButt(B butt){
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
	public abstract void fetchButtPoint(B butt, Vector3 dest);
	public abstract float convertToTargetRotation(double rad);

	@Override
	public void doJob(T target, long processTime) {
		
		approve = false;
		fetchButtPoint(butt, buttCenter);
/*		EntityPrint<?, C> buttPrint = butt.getSafePrint();
		fetchButtPoint(buttPrint.getOrigin().source, buttCenter);
		buttPrint.free();*/

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
		
		double d = AlgebraHelper.pow(b, 2) - 4 * a * c;
		if(d < 0){
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
			return;
		}
		float targetRotation = convertToTargetRotation(Math.acos(Math.sqrt(x)));
//		curRotationSpeed = rotationSpeed;
		float diff = targetRotation - nodeOperator.rotatedRadians();
		if(Math.abs(diff) > minDiff){
/*			if(diff < 0){
				curRotationSpeed *= -1;
			}*/
			float diffTime = processTime - lastCheckTime;
			float rRad = diffTime * rotationSpeed / 1000f;
			if(rRad > minDiff){
				if(rRad > Math.abs(diff)){
					rRad = diff;
				}else if(diff < 0){
					rRad *= -1;
				}
				lastCheckTime = processTime;
				nodeOperator.rotate(rRad);
				nodeOperator.updatePrint();
				subject.adjustCurrentPrint();
			}
		}else{
			approve = true;
		}
	}
	
	public boolean approve(T target, long processTime){
		return approve;
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}

}
