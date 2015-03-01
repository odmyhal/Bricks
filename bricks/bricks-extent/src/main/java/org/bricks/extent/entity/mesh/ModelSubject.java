package org.bricks.extent.entity.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * @deprecated use ModelSubjectOperable instead
 * Has exactly the same functionality as org.bricks.extent.entity.mesh.ModelSubjectOperable
 * @author oleh
 *
 * @param <E>
 * @param <I>
 */
public class ModelSubject<E extends Entity, I extends ModelSubjectPrint> extends Subject<E, I> implements RenderableProvider{

	private ModelInstance modelInstance;
	protected Matrix4 transform;
	protected boolean edit = true;
	private volatile int currentPrint = -1;
	private int lastPrint = -2;
	private Map<String, NodeOperator> operatedNodes = new HashMap<String, NodeOperator>();
	protected Map<Node, NodeData> dataNodes = new HashMap<Node, NodeData>();
	
	public ModelSubject(Brick brick, ModelInstance modelInstance, String... operNodes){
		super(brick);
		this.modelInstance = modelInstance;
		this.transform = new Matrix4();
		initiateNodeOperators(operNodes);
	}

	public ModelSubject(Brick brick, ModelInstance modelInstance, Matrix4 initialTransform, String... operNodes){
		super(brick);
		this.modelInstance = modelInstance;
		this.transform = initialTransform;
		initiateNodeOperators(operNodes);
	}
	
	private void initiateNodeOperators(String... nodes){
		for(String node: nodes){
			addNodeOperator(node);
		}
	}

	@Override
	public void translate(int x, int y){
		edit = true;
		super.translate(x, y);
		transform.trn((float) x, (float) y, 0f);
	}
	
	@Override
	public void rotate(float rad, Point central){
		edit = true;
		super.rotate(rad, central);
		transform.setToRotationRad(0, 0, 1f, rad);
		transform.trn(central.getFX(), central.getFY(), 0f);
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		int tmpCurPrint = currentPrint;
		if(!isLastPrint(lastPrint)){
			ModelSubjectPrint<?, ?> msp = getSafePrint();
			if(tmpCurPrint - msp.lastPrintModified < 2){
				modelInstance.transform.set(msp.transform);
			}
			for(Entry<Node, NodeData> entry : dataNodes.entrySet()){
				NodeData nd = entry.getValue();
				Node node = entry.getKey();
				NodeDataPrint ndView = msp.nodeData.get(node);
				if(nd.renderOutdated(ndView.lastPrintModified)){
					node.rotation.set(ndView.rotation);
					node.translation.set(ndView.translation);
					node.calculateTransforms(false);
				}
			}
			msp.free();
			lastPrint = tmpCurPrint;
		}
		modelInstance.getRenderables(renderables, pool);
	}
	
	public NodeOperator addNodeOperator(String operatorName, String... nodeNames){
		Validate.isTrue(nodeNames.length > 0, "Can't create node operator without nodes");
		List<Node> nodes = new ArrayList<Node>();
		List<String> names = Arrays.asList(nodeNames);
		for(Node node : modelInstance.nodes){
			if(names.contains(node.id)){
				nodes.add(node);
			}
		}
		Validate.isTrue(names.size() == nodes.size(), "Not all required nodes exists in modelInstance");
		NodeOperator nodeOperator = new NodeOperator(nodes.toArray(new Node[nodes.size()]));
		operatedNodes.put(operatorName, nodeOperator);
		for(Node node : nodes){
			NodeData nodeData = nodeOperator.getNodeData(node.id);
			Validate.isFalse(nodeData == null, "No node with id " + node.id);
			dataNodes.put(node, nodeData);
		}
		return nodeOperator;
	}
	
	private NodeOperator addNodeOperator(String nodeName){
		Node node = getNodeByName(nodeName);
		if(node == null){
			return null;
		}
		NodeOperator operator = new NodeOperator(node);
		operatedNodes.put(nodeName, operator);
		dataNodes.put(node, operator.getNodeData(nodeName));
		return operator;
	}
	
	public NodeOperator getNodeOperator(String operatorName){
		return operatedNodes.get(operatorName);
	}
	
	public Node getNodeByName(String nodeName){
		for(Node node : modelInstance.nodes){
			if(nodeName.equals(node.id)){
				return node;
			}
		}
		return null;
	}
	
	@Override
	public I print(){
		return (I) new ModelSubjectPrint(this.printStore);
	}
	
	@Override
	public int adjustCurrentPrint(){
		int res = super.adjustCurrentPrint();
		edit = false;
		currentPrint = res;
		return res;
	}
}
