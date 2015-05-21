package org.bricks.extent.subject.model;

import java.util.HashMap;
import java.util.Map;

import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;

import static org.bricks.engine.help.RotationHelper.rotationCycle;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/*
 * All operations should be processed in motor thread 
 * So we do not need synchronization
 */
public class NodeOperator implements RotationProvider{
	
	private Vector3 point = new Vector3();
	private Vector3 spin = new Vector3();
	
	private Quaternion helpQ = new Quaternion();
//	private Map<String, NodeData> nodes = new HashMap<String, NodeData>();
	private NodeData nodeData;
	private float rotatedRadians;
	private volatile float volatileRotatedRadians;

	public NodeOperator(Node node){
		nodeData = new NodeData(node);
/*		for(Node node : nodes){
			this.nodes.put(node.id, new NodeData(node));
		}*/
	}
	
	public NodeOperator(Vector3 spin, Vector3 point, Node node){
		this(node);
		setSpin(spin);
		setPoint(point);
	}
	
	public void setPoint(Vector3 point){
		this.point.set(point);
	}
	
	public void setSpin(Vector3 spin){
		this.spin.set(spin);
	}
	
	public void setRotatedRadians(float rad){
		this.rotatedRadians = rad;
		coerceRotation();
	}
	
	/*
	 * use this method in motor thread
	 */
	public float rotatedRadians(){
		return this.rotatedRadians;
	}
	
	/*
	 * Thread safe, use in render thread
	 */
	public float provideRotation(){
		return volatileRotatedRadians;
	}
	
	public void rotate(float rad){
		helpQ.setFromAxisRad(spin, rad);
		nodeData.rotateByPoint(helpQ, point);
/*		for(NodeData nd : nodes.values()){
			nd.rotateByPoint(helpQ, point);
		}*/
		this.rotatedRadians += rad;
		coerceRotation();
	}
	
	public void translate(float x, float y, float z){
		this.point.add(x, y, z);
		nodeData.translate(x, y, z);
/*		for(NodeData nd : nodes.values()){
			nd.translate(x, y, z);
		}*/
	}
	
	public void scale(Vector3 scl){
		this.scale(scl.x, scl.y, scl.z);
	}
	
	public void scale(float scaleX, float scaleY, float scaleZ){
		nodeData.scale(scaleX, scaleY, scaleZ, point);
/*		for(NodeData nd : nodes.values()){
			nd.scale(scaleX, scaleY, scaleZ, point);
		}*/
	}
	
	public void updatePrint(){
		nodeData.adjustCurrentPrint();
/*		for(NodeData nd : nodes.values()){
			nd.adjustCurrentPrint();
		}*/
	}
	
	public NodeData getNodeData(){
		return nodeData;
	}

	private void coerceRotation(){
		while(rotatedRadians < 0){
			rotatedRadians += rotationCycle;
		}
		while(rotatedRadians >= rotationCycle){
			rotatedRadians -= rotationCycle;
		}
		volatileRotatedRadians = rotatedRadians;
	}
	
	public Vector3 linkPoint(){
		return point;
	}
	
	public Vector3 linkSpin(){
		return spin;
	}
}
