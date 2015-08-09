package org.bricks.extent.processor.tbroll;

import org.bricks.core.help.AlgebraHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.processor.tool.Approver;
import org.bricks.engine.staff.Entity;
import org.bricks.extent.processor.NodeModifyProcessor;
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
public abstract class RollNodeToEntityVProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>>
	extends NodeModifyProcessor<T> 
	implements Approver<T>{
	
	protected Butt butt;
	protected boolean approve = false;
//	private MarkPoint rollPointMark;
	
	protected float rotationSpeed, /*curRotationSpeed,*/ bulletSpeed, bulletAcceleration;
	protected Vector3 buttCenter = new Vector3();

	public RollNodeToEntityVProcessor(T target, String nodeOperatorName) {
		super(target, nodeOperatorName);
		this.addSupplant(this.checkerType());
	}
	
	//Should be called in motor thread
	public void setButt(Butt butt){
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
	
	protected abstract Vector3 provideStartPoint(T target, long processTime);
//	protected abstract void fetchButtPoint(B butt, Vector3 dest);
	protected abstract float convertToTargetRotation(double rad);

	
	@Override
	public void doJob(T target, long processTime) {
		
		approve = false;
		butt.fetchOrigin(buttCenter);
		Vector3 myCenter = provideStartPoint(target, processTime);
		
		double dz = myCenter.z - buttCenter.z;
		double dz2 = AlgebraHelper.pow(dz, 2);
		double l2 = AlgebraHelper.pow(buttCenter.x - myCenter.x, 2) + AlgebraHelper.pow(buttCenter.y - myCenter.y, 2);
		double l4 = AlgebraHelper.pow(l2, 2);
		double s2 = AlgebraHelper.pow(this.bulletSpeed, 2);
		double s4 = AlgebraHelper.pow(s2, 2);
		double acc2 = AlgebraHelper.pow(this.bulletAcceleration, 2);
		
		double a = dz2 + l2;
		double b = ( dz * this.bulletAcceleration - s2 ) * l2 / s2;
		double c = acc2 * l4 / (4 * s4);
		
		double d = AlgebraHelper.pow(b, 2) - 4 * a * c;
		if(d < 0){
//			System.out.println("Wrong Discriminant " + d);
			lastCheckTime = processTime;
			return;
		}
		double ds = Math.sqrt(d);
		double x1 = (-b + ds) / Math.scalb(a, 1);
		double x2 = (-b - ds) / Math.scalb(a, 1);
		double x = Math.max(x1, x2);
		if(x < 0){
//			System.out.println("Wrong max Result " + x);
			lastCheckTime = processTime;
			return;
		}
		double t2 = l2 / (s2 * x);
		x = Math.sqrt(x);
		double xRad = Math.acos(x);
		if(this.bulletAcceleration * t2 / 2 > -dz){
			xRad *= -1;
		}
		float targetRotation = convertToTargetRotation(xRad);
		rotateToTarget(targetRotation, processTime);
	}
	
	protected void rotateToTarget(float targetRotation, long processTime){
		float diff = targetRotation - nodeOperator.rotatedRadians();
		if(Math.abs(diff) > minDiff){
			float diffTime = processTime - lastCheckTime;
			float rRad = diffTime * rotationSpeed / 1000f;
			if(rRad > minDiff){
				if(rRad > Math.abs(diff)){
					rRad = diff;
					approve = true;
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
