package org.bricks.extent.subject.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.exception.Validate;
import org.bricks.extent.tool.ModelHelper;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelBrickOperable<I extends MBOPrint<?>> extends ModelBrick<I>{
	
	private Map<String, NodeOperator> operatedNodes = new HashMap<String, NodeOperator>();
	protected Map<Node, NodeData> dataNodes = new HashMap<Node, NodeData>();

	public ModelBrickOperable(ModelInstance modelInstance, String... operNodes) {
		super(modelInstance);
		initiateNodeOperators(operNodes);
	}

	public void initiateNodeOperators(String... nodes){
		for(String node: nodes){
			addNodeOperator(node);
		}
	}
	
	public NodeOperator addNodeOperator(String operatorName, String... nodePaths){
		Validate.isTrue(nodePaths.length > 0, "Can't create node operator without nodes");
		Node[] nodes = new Node[nodePaths.length];
		int i = 0;
		for(String nodePath : nodePaths){
			Node node = findNode(nodePath, modelInstance.nodes);
			Validate.isFalse(node == null, "Could not find node by path: " + nodePath);
			nodes[i++] = node;
		}

		NodeOperator nodeOperator = new NodeOperator(nodes);
		operatedNodes.put(operatorName, nodeOperator);
		for(Node node : nodes){
			NodeData nodeData = nodeOperator.getNodeData(node.id);
			Validate.isFalse(nodeData == null, "No node with id " + node.id);
			dataNodes.put(node, nodeData);
		}
		return nodeOperator;
	}
	
	private Node findNode(String nodePath, Iterable<Node> nodes){
		int splitIndex = nodePath.indexOf('/');
		String curName;
		if(splitIndex < 0){
			curName = nodePath;
		}else{
			curName = nodePath.substring(0, splitIndex);
		}
		for(Node node : nodes){
			if(curName.equals(node.id)){
				if(curName.equals(nodePath)){
					return node;
				}else{
					return findNode(nodePath.substring(splitIndex + 1, nodePath.length()), node.children);
				}
			}
		}
		return null;
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
	protected void updateModelTransform(I modelBrickPrint){
		super.updateModelTransform(modelBrickPrint);
		for(Entry<Node, NodeData> entry : dataNodes.entrySet()){
			NodeData nd = entry.getValue();
			Node node = entry.getKey();
			NodeDataPrint ndView = modelBrickPrint.nodeData.get(node);
			if(nd.renderOutdated(ndView.lastPrintModified)){
				node.localTransform.set(ndView.transform);
				ModelHelper.calculateNodeGlobalTransforms(node);
			}
		}
	}
	
	@Override
	public I print(){
		return (I) new MBOPrint(this.printStore);
	}
}
