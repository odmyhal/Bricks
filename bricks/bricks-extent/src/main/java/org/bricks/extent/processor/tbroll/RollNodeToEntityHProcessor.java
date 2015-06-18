package org.bricks.extent.processor.tbroll;

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
public abstract class RollNodeToEntityHProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>>
	extends NodeModifyProcessor<T>
	implements Approver<T>{
	
	
	
	private Butt butt;
//	private MarkPoint rollPointMark;
	private Vector3 buttOrigin = new Vector3();
	private Fpoint /*buttOrigin = new Fpoint(), buttArrow = new Fpoint(),*/ rollOrigin = new Fpoint();
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
		this.supplant(this.checkerType());
	}
	
	public void setButt(Butt butt){
		this.butt = butt;
	}
	
	public void setRotationSpeed(float rs){
		rotationSpeed = rs;
	}
	
	
	protected abstract void fetchRollOrigin(Fpoint dest);
	protected abstract float provideAbsoluteCurrentRotation();
	
	protected void fetchButtPoint(Butt butt, Vector3 dest){
		butt.fetchOrigin(dest);
	}

//	int log = 0;
	@Override
	public void doJob(T target, long processTime) {
//		checkSpin();
		approve = false;
//		butt.fetchOrigin(buttOrigin);
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
		buttOrigin.z = 0;
//		PointHelper.normalize(buttOrigin);
		buttOrigin.nor();
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
