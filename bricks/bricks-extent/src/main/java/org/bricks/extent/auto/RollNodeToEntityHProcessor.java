package org.bricks.extent.auto;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.help.AlgebraHelper;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;
import org.bricks.exception.Validate;
import org.bricks.extent.engine.checker.NodeModifyProcessor;
import org.bricks.extent.space.overlap.MarkPoint;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public abstract class RollNodeToEntityHProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, C>
	extends NodeModifyProcessor<T>{
	
	
	
	private Entity<EntityPrint<?, C>> butt;
	private MarkPoint rollPointMark;
	private Fpoint buttOrigin = new Fpoint(), buttArrow = new Fpoint();
	private float rotationSpeed, curRotationSpeed;

	/**
	 * Like central point of nodeOperator arrow point should be in coordinates of node parent
	 * @param target
	 * @param nodeOperatorName
	 * @param arrow
	 * @param linkMatrices
	 
	public RollNodeToEntityProcessor(T target, String nodeOperatorName, Vector3 arrow, Matrix4... linkMatrices) {
		super(target, nodeOperatorName);
		Vector3 rollCenter = new Vector3(this.nodeOperator.linkPoint());
		//Mult rollCenter to local matrix of current node 
		Matrix4 invMatrix = new Matrix4(linkMatrices[linkMatrices.length - 1]);
		invMatrix.inv();
		rollCenter.mul(invMatrix);
		arrow.mul(invMatrix);
		rollPointMark = new MarkPoint(rollCenter, arrow);
		for(Matrix4 matrix: linkMatrices){
			rollPointMark.addTransform(matrix);
		}
		// TODO Auto-generated constructor stub
	}
	*/
	
	/**
	 * 
	 * @param target
	 * @param nodeOperatorName
	 * @param radArrow arrow in radian from central point
	 * @param linkMatrices
	 */
	public RollNodeToEntityHProcessor(T target, String nodeOperatorName, double radArrow, Matrix4... linkMatrices) {
		super(target, nodeOperatorName);
		Vector3 rollCenter = new Vector3(this.nodeOperator.linkPoint());
		//Mult rollCenter to local matrix of current node 
		Matrix4 invMatrix = new Matrix4(linkMatrices[linkMatrices.length - 1]);
		invMatrix.inv();
		rollCenter.mul(invMatrix);
		
		Vector3 arrow = new Vector3(rollCenter.x + (float) (1000f * Math.cos(radArrow)), 
				(float) (rollCenter.y + 100f * Math.sin(radArrow)), rollCenter.z);
		arrow.mul(invMatrix);
		rollPointMark = new MarkPoint(rollCenter, arrow);
		for(Matrix4 matrix: linkMatrices){
			rollPointMark.addTransform(matrix);
		}
		// TODO Auto-generated constructor stub
	}
	
	public void setButt(Entity butt){
		this.butt = butt;
	}
	
	public void setRotationSpeed(float rs){
		rotationSpeed = rs;
	}
	
	public abstract void fetchButtPoint(C src, Fpoint dest);

	@Override
	public void doJob(T target, long processTime) {
//		checkSpin();
		EntityPrint<?, C> buttPrint = butt.getSafePrint();
		fetchButtPoint(buttPrint.getOrigin().source, buttOrigin);
		buttPrint.free();
		
		rollPointMark.calculateTransforms();
		Vector3 rollOrigin = rollPointMark.getMark(0);
		
		Vector3 arrow = rollPointMark.getMark(1);
		buttArrow.setX(arrow.x - rollOrigin.x);
		buttArrow.setY(arrow.y - rollOrigin.y);
		PointHelper.normalize(buttArrow);
		float currentRotation = (float) AlgebraHelper.trigToRadians(buttArrow.x, buttArrow.y);
		
		buttOrigin.x -= rollOrigin.x;
		buttOrigin.y -= rollOrigin.y;
		PointHelper.normalize(buttOrigin);
		float targetRotation = (float) AlgebraHelper.trigToRadians(buttOrigin.x, buttOrigin.y);
		float rotationDiff = targetRotation - currentRotation;
		if(Math.abs(rotationDiff) < minDiff){
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
			lastCheckTime = processTime;
			nodeOperator.rotate(rRad);
			nodeOperator.updatePrint();
			subject.adjustCurrentPrint();
//			target.setUpdate();
		}
	}
/*	
	private void checkSpin(){
		Vector3 spin = this.nodeOperator.linkSpin();
		Validate.isTrue(spin.x == 0 && spin.y == 0 && spin.z > 0, "Spin(" + spin + ") has to be normal to horizontal plane");
	}
*/
	@Override
	public boolean stopCondition(T target, long processTime) {
		return false;
	}

	public static class FpointProcessor<T extends MultiLiver<? extends ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>>
		extends RollNodeToEntityHProcessor<T, Fpoint>{

		public FpointProcessor(T target, String nodeOperatorName, double arrowRad, Matrix4... linkMatrices) {
			super(target, nodeOperatorName, arrowRad, linkMatrices);
		}

		@Override
		public void fetchButtPoint(Fpoint src, Fpoint dest) {
			dest.x = src.x;
			dest.y = src.y;
		}
	}
}
