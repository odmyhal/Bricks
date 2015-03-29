package org.bricks.extent.engine.checker;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.event.processor.WorkToConditionProcessor;
import org.bricks.engine.item.MultiLiver;
import org.bricks.exception.Validate;
import org.bricks.extent.entity.mesh.ModelSubjectOperable;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;
import org.bricks.extent.subject.model.NodeOperator;

public abstract class NodeModifyProcessor<T extends MultiLiver<ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>> extends WorkToConditionProcessor<T> {
	protected ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable> subject;
	protected NodeOperator nodeOperator;
	protected long lastCheckTime;
	protected T checkEntity;
	protected String nodeOperatorName;

	public NodeModifyProcessor(T target, CheckerType type, String nodeOperatorName) {
		super(type);
		initTargetOperator(target, nodeOperatorName);
	}
	
	protected void initTargetOperator(T target, String nodeOperatorName){
		for(ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable> ms : target.getStaff() ){
			nodeOperator = ms.linkModelBrick().getNodeOperator(nodeOperatorName);
			if(nodeOperator != null){
				subject = ms;
				break;
			}
		}
		Validate.isFalse(nodeOperator == null, target.getClass().getCanonicalName() +
				" could not find operator by name \"" + nodeOperatorName + "\"");
		this.checkEntity = target;
		this.nodeOperatorName = nodeOperatorName;
	}
	
	public NodeOperator getOperator(){
		return nodeOperator;
	}
	
	@Override
	public void activate(T target, long curTime){
		Validate.isTrue(checkEntity.equals(target));
		lastCheckTime = curTime;
		super.activate(target, curTime);
	}
}
