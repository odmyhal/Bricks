package org.bricks.extent.entity.subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.exception.Validate;
import org.bricks.extent.entity.mesh.NodeData;
import org.bricks.extent.entity.mesh.NodeDataView;
import org.bricks.extent.entity.mesh.NodeOperator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelSubject<E extends Entity> extends Subject<E> implements RenderableProvider{

	private ModelInstance modelInstance;
	private Matrix4 transform;
	private boolean edit = false;
	private Map<String, NodeOperator> operatedNodes = new HashMap<String, NodeOperator>();
	private Map<Node, NodeData> dataNodes = new HashMap<Node, NodeData>();
	
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
		super.translate(x, y);
		synchronized(transform){
			transform.trn((float) x, (float) y, 0f);
			edit = true;
		}
	}
	
	@Override
	public void rotate(float rad, Point central){
		super.rotate(rad, central);
		synchronized(transform){
			transform.setToRotationRad(0, 0, 1f, rad);
			transform.trn(central.getFX(), central.getFY(), 0f);
			edit = true;
		}
		
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		synchronized(transform){
			if(edit){
				modelInstance.transform.set(transform);
				edit = false;
			}
		}
		updateNodes();
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
	
	private void updateNodes(){
		for(Entry<Node, NodeData> entry : dataNodes.entrySet()){
			Node node = entry.getKey();
			NodeDataView ndView = entry.getValue().getCurrentView();
			node.rotation.set(ndView.rotation);
			node.translation.set(ndView.translation);
			node.calculateTransforms(false);
			ndView.free();
		}
	}
}
