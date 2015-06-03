package org.bricks.extent.engine.processor;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.processor.tool.Approver;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;
import org.bricks.exception.Validate;
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
public abstract class RollNodeToEntityHProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, B extends Entity<?>>
	extends NodeModifyProcessor<T>
	implements Approver<T>{
	
	
	
	private B butt;
//	private MarkPoint rollPointMark;
	private Fpoint buttOrigin = new Fpoint(), /*buttArrow = new Fpoint(),*/ rollOrigin = new Fpoint();
	private float rotationSpeed, curRotationSpeed;
	private boolean approve = false;
	
	/**
	 * 
	 * @param target
	 * @param nodeOperatorName
	 * @param radArrow arrow in radian from central point
	 * @param linkMatrices
	 */
	public RollNodeToEntityHProcessor(T target, String nodeOperatorName/*, double radArrow, Matrix4... linkMatrices*/) {
		super(target, nodeOperatorName);
/*		Vector3 rollCenter = new Vector3(this.nodeOperator.linkPoint());
		//Mult rollCenter to local matrix of current node 
		Matrix4 invMatrix = new Matrix4(linkMatrices[linkMatrices.length - 1]);
		invMatrix.inv();
		rollCenter.mul(invMatrix);
		
		Vector3 arrow = new Vector3(rollCenter.x + (float) (1000f * Math.cos(radArrow)), 
				(float) (rollCenter.y + 1000f * Math.sin(radArrow)), rollCenter.z);
		arrow.mul(invMatrix);
		rollPointMark = new MarkPoint(rollCenter, arrow);
		for(Matrix4 matrix: linkMatrices){
			rollPointMark.addTransform(matrix);
		}
		// TODO Auto-generated constructor stub*/
	}
	
	public void setButt(B butt){
		this.butt = butt;
	}
	
	public void setRotationSpeed(float rs){
		rotationSpeed = rs;
	}
	
	protected abstract void fetchButtPoint(B butt, Fpoint dest);
	protected abstract void fetchRollOrigin(Fpoint dest);
	protected abstract float provideAbsoluteCurrentRotation();

//	int log = 0;
	@Override
	public void doJob(T target, long processTime) {
//		checkSpin();
		approve = false;
		fetchButtPoint(butt, buttOrigin);
/*		EntityPrint<?, C> buttPrint = butt.getSafePrint();
		fetchButtPoint(buttPrint.getOrigin().source, buttOrigin);
		buttPrint.free();*/
		
//		rollPointMark.calculateTransforms();
//		Vector3 rollOrigin = rollPointMark.getMark(0);
		
		fetchRollOrigin(rollOrigin);
/*		
		Vector3 arrow = rollPointMark.getMark(1);
		buttArrow.setX(arrow.x - rollOrigin.x);
		buttArrow.setY(arrow.y - rollOrigin.y);
		PointHelper.normalize(buttArrow);
		float currentRotation = (float) AlgebraHelper.trigToRadians(buttArrow.x, buttArrow.y);
		
*/

		float currentRotation = provideAbsoluteCurrentRotation();
		
		buttOrigin.x -= rollOrigin.x;
		buttOrigin.y -= rollOrigin.y;
		PointHelper.normalize(buttOrigin);
		float targetRotation = (float) AlgebraHelper.trigToRadians(buttOrigin.x, buttOrigin.y);
		float rotationDiff = targetRotation - currentRotation;
/*
		if(++log > 1000){
			System.out.println("currentRotation " + currentRotation + ", targetRotation: " + targetRotation);
			log = 0;
		}*/
		float absRotationDiff = Math.abs(rotationDiff);
		if(absRotationDiff < minDiff){
			lastCheckTime = processTime;
			return;
		}
		
		curRotationSpeed = rotationSpeed;
		if(rotationDiff > minDiff){
			if(rotationDiff > Math.PI){
				curRotationSpeed *= -1;
			}
		}else if(rotationDiff < -minDiff){
			if(rotationDiff > -Math.PI){
				curRotationSpeed *= -1;
			}
		}
		
		float diffTime = processTime - lastCheckTime;
		float rRad = diffTime * curRotationSpeed / 1000f;
		if(Math.abs(rRad) > minDiff){
			if(Math.abs(rRad) > absRotationDiff){
				if(rRad > 0){
					rRad = absRotationDiff;
				}else{
					rRad = -absRotationDiff;
				}
			}
			lastCheckTime = processTime;
			nodeOperator.rotate(rRad);
			nodeOperator.updatePrint();
			subject.adjustCurrentPrint();
//			target.setUpdate();
		}else{
			approve = true;
		}
	}
/*	
	private void checkSpin(){
		Vector3 spin = this.nodeOperator.linkSpin();
		Validate.isTrue(spin.x == 0 && spin.y == 0 && spin.z > 0, "Spin(" + spin + ") has to be normal to horizontal plane");
	}
*/
	public boolean approve(T target, long processTime){
		return approve;
	}
	
	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}
/*
	public abstract static class FpointProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>>
		extends RollNodeToEntityHProcessor<T, Fpoint>{

		public FpointProcessor(T target, String nodeOperatorName) {
			super(target, nodeOperatorName);
		}

		@Override
		public void fetchButtPoint(Fpoint src, Fpoint dest) {
			dest.x = src.x;
			dest.y = src.y;
		}
	}*/
}
