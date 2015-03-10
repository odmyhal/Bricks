package org.bricks.extent.entity.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.staff.Entity;
import org.bricks.exception.Validate;
import org.bricks.extent.tool.ModelHelper;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelSubjectOperable<E extends Entity, I extends ModelSubjectPrint> extends ModelSubjectBase<E, I>{

	private volatile int currentPrintVolatile = -1;
	private int lastPrint = -2, currentPrint = -3, renderPrintModified = -5;
	protected int lastPrintModified = -4;
	private Map<String, NodeOperator> operatedNodes = new HashMap<String, NodeOperator>();
	protected Map<Node, NodeData> dataNodes = new HashMap<Node, NodeData>();
	
	public ModelSubjectOperable(Brick brick, ModelInstance modelInstance, String... operNodes){
		super(brick, modelInstance);
		initiateNodeOperators(operNodes);
	}
/*
	public ModelSubjectOperable(Brick brick, ModelInstance modelInstance, Matrix4 initialTransform, String... operNodes){
		super(brick, modelInstance, initialTransform);
		initiateNodeOperators(operNodes);
	}
*/	
	private void initiateNodeOperators(String... nodes){
		for(String node: nodes){
			addNodeOperator(node);
		}
	}

	@Override
	public void translate(int x, int y){
//		edit = true;
		lastPrintModified = currentPrint;
		super.translate(x, y);
		transform.trn((float) x, (float) y, 0f);
	}
	
	@Override
	public void rotate(float rad, Point central){
//		edit = true;
		lastPrintModified = currentPrint;
		super.rotate(rad, central);
		transform.setToRotationRad(0, 0, 1f, rad);
		transform.trn(central.getFX(), central.getFY(), 0f);
	}
//	int testTmp = 0;
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		int tmpCurPrint = currentPrintVolatile;
		/*
		 * check if we already updated current print
		 */
		if(!isLastPrint(lastPrint)){
			ModelSubjectPrint<?, ?> msp = getSafePrint();
			Validate.isFalse(renderPrintModified > msp.lastPrintModified, "printModified can't be less than render last modified");
			if(renderPrintModified < msp.lastPrintModified){
				modelInstance.transform.set(msp.transform);
				renderPrintModified = msp.lastPrintModified;
			}
/*			if(++testTmp < 4){
				System.out.println("Render matrix transform: " + msp.transform);
			}*/
			for(Entry<Node, NodeData> entry : dataNodes.entrySet()){
				NodeData nd = entry.getValue();
				Node node = entry.getKey();
				NodeDataPrint ndView = msp.nodeData.get(node);
				if(nd.renderOutdated(ndView.lastPrintModified)){
//					node.rotation.set(ndView.rotation);
//					node.translation.set(ndView.translation);
					node.localTransform.set(ndView.transform);
					ModelHelper.calculateNodeGlobalTransforms(node);
//					node.calculateTransforms(true);
				}
			}
			msp.free();
			lastPrint = tmpCurPrint;
		}
		modelInstance.getRenderables(renderables, pool);
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
	public I print(){
		return (I) new ModelSubjectPrint(this.printStore);
	}
	
	@Override
	public int adjustCurrentPrint(){
		currentPrint = super.adjustCurrentPrint();
//		System.out.println("ModelSubjectOperable adjusted currentPrint " + edit);
//		edit = false;
		currentPrintVolatile = currentPrint;
		return currentPrint;
	}
}
