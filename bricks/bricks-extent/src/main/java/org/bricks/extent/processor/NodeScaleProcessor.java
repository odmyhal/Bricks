package org.bricks.extent.processor;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.processor.WorkToConditionProcessor;
import org.bricks.extent.entity.mesh.ModelSubjectOperable;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Vector3;

public class NodeScaleProcessor<T extends MultiLiver<ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>> extends NodeModifyProcessor<T> {
	
	private Vector3 targetScale = new Vector3();
	private long scaleTime;

	private Vector3 initScale = new Vector3();
	private volatile long initTime;
	
	private Vector3 scaleSpeed = new Vector3();
	private Vector3 helpV = new Vector3();
	private boolean finish = false;
	 
	public NodeScaleProcessor(T target, String nodeOperatorName){
		super(target, CheckerType.registerCheckerType(), nodeOperatorName);
		addSupplant(checkerType());
	} 
	
	public void init(Vector3 tScale, long sTime){
		this.init(tScale.x, tScale.y, tScale.z, sTime);
	}
	
	public void init(float targetScaleX, float targetScaleY, float targetScaleZ, long sTime){
		initScale.set(targetScaleX, targetScaleY, targetScaleZ);
		initTime = sTime;
	}
	
	@Override
	public void doJob(T target, long processTime) {
		long diffTime = processTime - this.lastCheckTime;
		if(diffTime > 20){
			this.nodeOperator.getNodeData(/*this.nodeOperatorName*/).flushScale(helpV);
			helpV.x = 1f + (scaleSpeed.x * diffTime) / helpV.x;
			helpV.y = 1f + (scaleSpeed.y * diffTime) / helpV.y;
			helpV.z = 1f + (scaleSpeed.z * diffTime) / helpV.z;
			this.nodeOperator.scale(helpV);
			this.lastCheckTime += diffTime;
			checkFinish();
			this.nodeOperator.updatePrint();
			this.subject.adjustCurrentPrint();
		}
	}
	
	private void checkFinish(){
		this.nodeOperator.getNodeData(/*this.nodeOperatorName*/).flushScale(helpV);
		if( checkSingleOrth(helpV.x, targetScale.x, scaleSpeed.x)
				|| checkSingleOrth(helpV.y, targetScale.y, scaleSpeed.y)
				|| checkSingleOrth(helpV.z, targetScale.z, scaleSpeed.z)
				){
			helpV.x = targetScale.x / helpV.x;
			helpV.y = targetScale.y / helpV.y;
			helpV.z = targetScale.z / helpV.z;
			this.nodeOperator.scale(helpV);
			finish = true;
		}
	}
	
	private boolean checkSingleOrth(float cur, float target, float speed){
		if(speed > 0){
			if(cur < target){
				return false;
			}else{
				return true;
			}
		}else if(speed < 0){
			if(cur > target){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		return finish;
	}

	@Override
	public void activate(T target, long curTime){
		/*
		 * Flush cached values via volatile
		 */
		targetScale.set(initScale);
//		System.out.print("Activated NodeScaleProcessor target: " + targetScale + ", scaleTime: " + initTime);
		scaleTime = initTime;
		this.nodeOperator.getNodeData(/*this.nodeOperatorName*/).flushScale(scaleSpeed);
//		System.out.print(", current scale: " + scaleSpeed);
		scaleSpeed.sub(targetScale);
		scaleSpeed.scl(-1f / scaleTime);
//		System.out.println(", Scale speed: " + scaleSpeed);
		super.activate(target, curTime);
	}
	
}
