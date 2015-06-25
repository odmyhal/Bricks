package org.bricks.extent.control;

import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.extent.entity.mesh.ModelSubjectOperable;
import org.bricks.extent.processor.NodeRollProcessor;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.ModelBrickSubject;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;

import com.badlogic.gdx.math.Vector2;

public class NodeRollProcessorAction<T extends MultiLiver<ModelBrickSubject<?, ?, ?, ?, ModelBrickOperable>, ?, ?>, W extends FlowTouchPad> extends EventCheckRegAction<T, W> {

	private Loop<NodeRollProcessor<T>> nodeRollProcessors;
	private Vector2 touchPercentile = new Vector2();
	
	private float rotationSpeed;
//	private RotationProvider rotationProvider;
	
	public NodeRollProcessorAction(T target, float rotationSpeed, String... nodeOperatorNames) {
		super(target);
		nodeRollProcessors = new LinkLoop<NodeRollProcessor<T>>();
		for(int i = 0; i < nodeOperatorNames.length; i++){
			nodeRollProcessors.add(new NodeRollProcessor(target, nodeOperatorNames[i]));
		}
//		nodeRollProcessor = new NodeRollProcessor(target, nodeOperatorName);
//		this.rotationProvider = nodeRollProcessor.getOperator();
		this.rotationSpeed = rotationSpeed;
	}
	
	@Override
	public void init(W widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		float tarRad = (float)AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		for(NodeRollProcessor nodeRollProcessor : nodeRollProcessors){
			RotationHelper.calculateRotationData(nodeRollProcessor.getOperator().provideRotation(), tarRad, rotationSpeed);
			float curRotationSpeed = RotationHelper.getCalculatedRotationSpeed();
			float targetRotation = RotationHelper.getCalculatedTargetRotation();
			
			nodeRollProcessor.init(targetRotation, curRotationSpeed);
			addChecker(nodeRollProcessor);
		}
/*		RotationHelper.calculateRotationData(rotationProvider.provideRotation(), tarRad, rotationSpeed);
		float curRotationSpeed = RotationHelper.getCalculatedRotationSpeed();
		float targetRotation = RotationHelper.getCalculatedTargetRotation();
		
		nodeRollProcessor.init(targetRotation, curRotationSpeed);
		addChecker(nodeRollProcessor);*/
	}

}
