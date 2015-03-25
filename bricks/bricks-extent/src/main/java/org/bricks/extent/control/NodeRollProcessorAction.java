package org.bricks.extent.control;

import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.item.MultiLiver;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.extent.engine.checker.NodeRollProcessor;
import org.bricks.extent.entity.mesh.ModelSubjectOperable;
import org.bricks.extent.subject.model.ModelBrickOperable;

import com.badlogic.gdx.math.Vector2;

public class NodeRollProcessorAction<T extends MultiLiver<ModelSubjectOperable<?, ?, ModelBrickOperable>, ?, ?>, W extends FlowTouchPad> extends EventCheckRegAction<T, W> {

	private NodeRollProcessor<T> nodeRollProcessor;
	private Vector2 touchPercentile = new Vector2();
	
	private float rotationSpeed;
	private RotationProvider rotationProvider;
	
	public NodeRollProcessorAction(T target, String nodeOperatorName, float rotationSpeed) {
		super(target);
		nodeRollProcessor = new NodeRollProcessor(target, nodeOperatorName);
		this.rotationProvider = nodeRollProcessor.getOperator();
		this.rotationSpeed = rotationSpeed;
	}
	
	@Override
	public void init(W widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		float tarRad = (float)AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		RotationHelper.calculateRotationData(rotationProvider.provideRotation(), tarRad, rotationSpeed);
		float curRotationSpeed = RotationHelper.getCalculatedRotationSpeed();
		float targetRotation = RotationHelper.getCalculatedTargetRotation();
		
		nodeRollProcessor.init(targetRotation, curRotationSpeed);
		addChecker(nodeRollProcessor);
	}

}
