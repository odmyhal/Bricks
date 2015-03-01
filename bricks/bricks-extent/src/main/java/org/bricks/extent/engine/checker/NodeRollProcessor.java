package org.bricks.extent.engine.checker;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.processor.WorkToConditionProcessor;
import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.exception.Validate;
import org.bricks.extent.entity.mesh.ModelSubjectOperable;
import org.bricks.extent.entity.mesh.NodeOperator;

public class NodeRollProcessor<T extends MultiLiver<ModelSubjectOperable, ?>> extends WorkToConditionProcessor<T>{
	
	private static final float minRotationRad = (float) Math.PI / 180;
//	private static final CheckerType NODE_ROLL_CH_TYPE = CheckerType.registerCheckerType();
	private ModelSubjectOperable subject;
//	private AtomicInteger one;
//	private volatile boolean inited = false;
	private float targetRotation, rotationSpeed, tmpRotation;
	//One of tmpRotation, tmpSpeed should be volatile
	private volatile float tmpSpeed;
	private NodeOperator nodeOperator;
	private long lastCheckTime;
	private T checkEntity;
	
	public NodeRollProcessor(T target, String nodeOperatorName){
		super(CheckerType.registerCheckerType());
		supplant(checkerType());
		for(ModelSubjectOperable ms : target.getStaff() ){
			nodeOperator = ms.getNodeOperator(nodeOperatorName);
			if(nodeOperator != null){
				subject = ms;
				break;
			}
		}
		Validate.isFalse(nodeOperator == null, target.getClass().getCanonicalName() +
				" could not find operator by name \"" + nodeOperatorName + "\"");
		this.checkEntity = target;
	}
	
	public NodeOperator getOperator(){
		return nodeOperator;
	}

	@Override
	public void doJob(T target, long processTime) {
		Validate.isTrue(lastCheckTime <= processTime);
		Validate.isFalse(rotationSpeed == 0f);
		float diffTime = processTime - lastCheckTime;
		float rRad = diffTime * rotationSpeed / 1000f;
		if(Math.abs(rRad) > minRotationRad){
			lastCheckTime = processTime;
			nodeOperator.rotate(rRad);
			nodeOperator.updatePrint();
			subject.adjustCurrentPrint();
		}
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		float currentRotation = nodeOperator.rotatedRadians();
		boolean stop = RotationHelper.isRotationFinished(rotationSpeed, currentRotation, targetRotation);
		if(stop){
			float rDiff = currentRotation - targetRotation;
			if(Math.abs(rDiff) > Math.PI / (180 * 10)){
				nodeOperator.rotate(rDiff);
			}
		}
		return stop;
	}
	
	public void init(float targetRotation, float rSpeed){
		this.tmpRotation = targetRotation;
		/*
		 * Flush cached values via volatile
		 */
		this.tmpSpeed = rSpeed;
//		inited = true;
//		System.out.println(this.getClass().getCanonicalName() + " inited...");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bricks.engine.event.check.ChunkEventChecker#activate()
	 * Called in motor thread
	 * startSpeed is volatile because of it is changed in render thread
	 */
	@Override
	public void activate(T target, long curTime){
		Validate.isTrue(checkEntity.equals(target));
//		Validate.isTrue(inited, "Processor should be inited before activated");
		/*
		 * Flush cached values via volatile
		 */
		this.rotationSpeed = this.tmpSpeed;
		this.targetRotation = this.tmpRotation;
//		inited = false;
		lastCheckTime = curTime;
		super.activate(target, curTime);
	}

}
