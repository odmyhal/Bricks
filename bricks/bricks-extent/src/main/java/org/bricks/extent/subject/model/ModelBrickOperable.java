package org.bricks.extent.subject.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.exception.Validate;
import org.bricks.extent.tool.ModelHelper;
import org.bricks.utils.LoopMap;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelBrickOperable<I extends MBOPrint<?>> extends ModelBrick<I>{
	
	private Map<String, NodeOperator> operatedNodes = new HashMap<String, NodeOperator>();
	protected LoopMap<Node, NodeData> dataNodes = new LoopMap.MultiThreadIterable<Node, NodeData>();

	public ModelBrickOperable(ModelInstance modelInstance, String... operNodes) {
		super(modelInstance);
		initiateNodeOperators(operNodes);
	}
	
	public void reset(){
		for(NodeData nodeData : dataNodes){
			Node initialNode = ModelHelper.findNode(nodeData.nodePath, this.modelInstance.model.nodes);
			Validate.isFalse(initialNode == null, "Model has no node by path " + nodeData.nodePath);
			nodeData.reset(initialNode.rotation, initialNode.translation, initialNode.scale);
		}
		super.reset();
	}

	public void initiateNodeOperators(String... nodesPath){
		for(String node: nodesPath){
			NodeOperator no = addNodeOperator(node, node);
			Validate.isFalse(no == null, "Could not find node by path: " + node);
		}
	}
	
	public NodeOperator addNodeOperator(String operatorName, String nodePath){
		Node node = ModelHelper.findNode(nodePath, modelInstance.nodes); 
		Validate.isFalse(node == null, "Could not find node by path: " + nodePath);

		NodeOperator nodeOperator = new NodeOperator(node, nodePath);
		operatedNodes.put(operatorName, nodeOperator);
		dataNodes.put(node, nodeOperator.getNodeData());
		return nodeOperator;
	}
/*
	private NodeOperator addNodeOperator(String nodeName){
		Node node = getNodeByName(nodeName);
		if(node == null){
			return null;
		}
		NodeOperator operator = new NodeOperator(node);
		operatedNodes.put(nodeName, operator);
		dataNodes.put(node, operator.getNodeData());
		return operator;
	}
*/	
	public NodeOperator getNodeOperator(String operatorName){
		return operatedNodes.get(operatorName);
	}
/*	
	public Node getNodeByName(String nodeName){
		for(Node node : modelInstance.nodes){
			if(nodeName.equals(node.id)){
				return node;
			}
		}
		return null;
	}
*/	
	@Override
	protected void updateModelTransform(I modelBrickPrint){
		super.updateModelTransform(modelBrickPrint);
		for(Entry<Node, NodeData> entry : dataNodes.entryLoop()){
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
		MBOPrint mbp = new MBOPrint(this.printStore);
		return (I) mbp;
	}
}
