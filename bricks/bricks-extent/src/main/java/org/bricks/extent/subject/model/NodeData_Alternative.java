package org.bricks.extent.subject.model;

import org.bricks.engine.neve.PrintableBase;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeData_Alternative<I extends NodeDataPrint> extends PrintableBase<I>{


	private int currentPrint = -1, renderLastPrint = -5;
	protected int lastPrintModified = -2;
	
//	private final Quaternion rotation = new Quaternion();
//	private final Vector3 translation = new Vector3();
//	private final Vector3 scale = new Vector3();
	private final Matrix4 transformMatrix = new Matrix4();
	
	private final Matrix4 helpMatrix = new Matrix4();
	private final Matrix4 resultMatrix = new Matrix4();
	
	public NodeData_Alternative(Node node){
		this(node.rotation, node.translation, node.scale);
	}
	
	public NodeData_Alternative(Quaternion rotation, Vector3 translation, Vector3 scale){
		this.transformMatrix.set(translation, rotation, scale);
//		this.rotation.set(rotation);
//		this.translation.set(translation);
//		this.scale.set(scale);
		initPrintStore();
		adjustCurrentPrint();
	}
/*	
	public void rotateByPoint(Quaternion q, Vector3 point){
		this.rotation.mulLeft(q);
		q.toMatrix(helpMatrix.val);
		this.translation.sub(point);
		this.translation.mul(helpMatrix);
		translate(point);
	}
	*/
	public void rotateByPoint(Quaternion q, Vector3 point){
		translate(-point.x, -point.y, -point.z);
		q.toMatrix(helpMatrix.val);
		transformMatrix.mulLeft(helpMatrix);
		translate(point);
	}
	
	public void translate(Vector3 go){
		translate(go.x, go.y, go.z);
//		edit = true;
		lastPrintModified = currentPrint;
	}
	
	protected void translate(float x, float y, float z){
		helpMatrix.idt().trn(x, y, z);
		transformMatrix.mulLeft(helpMatrix);
	}
	
	public void scale(Vector3 scl, Vector3 point){
		this.scale(scl.x, scl.y, scl.z, point);
	}
	
	public void scale(float scaleX, float scaleY, float scaleZ, Vector3 point){
		transformMatrix.scale(scaleX, scaleY, scaleZ);
		lastPrintModified = currentPrint;
	}

	protected boolean renderOutdated(int modifiedPrint){
		if(renderLastPrint < modifiedPrint){
			renderLastPrint = modifiedPrint;
			return true;
		}
		return false;
	}

	@Override
	public int adjustCurrentPrint() {
		currentPrint = printStore.adjustCurrentPrint();
		return currentPrint;
	}
	
	/*
	 * Method used in NodeDataPrint.init() called from this.adjustCurrentPrint()
	 */
	protected void calculateTransform(){
		resultMatrix.set(transformMatrix);
	}
	
	public void flushScale(Vector3 dest){
		this.transformMatrix.getScale(dest);
	}
	
	/**
	 * For concurrency safe sake use only in owner Motor thread
	 * @return link to transform matrix
	 */
	public Matrix4 linkTransform(){
		return resultMatrix;
	}

	public I print() {
		return (I) new NodeDataPrint(printStore);
	}
}
