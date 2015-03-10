package org.bricks.extent.entity.mesh;

import org.bricks.engine.neve.PrintableBase;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeData<I extends NodeDataPrint> extends PrintableBase<I>{
	
//	private PrintStore<?, I> printStore;
//	protected boolean edit = true;
//	private NodeDataView view;
//	private LinkedList<NodeDataView> viewCache = new LinkedList<NodeDataView>();
	private int currentPrint = -1, renderLastPrint = -5;
	protected int lastPrintModified = -2;
	
	private final Quaternion rotation = new Quaternion();
	private final Vector3 translation = new Vector3();
	private final Vector3 scale = new Vector3();
	private final Matrix4 transformMatrix = new Matrix4();
	
	private Matrix4 helpMatrix = new Matrix4();
//	private Vector3 helpPoint = new Vector3();
	
	public NodeData(Node node){
		this(node.rotation, node.translation, node.scale);
	}
	
	public NodeData(Quaternion rotation, Vector3 translation, Vector3 scale){
		this.rotation.set(rotation);
		this.translation.set(translation);
		this.scale.set(scale);
		initPrintStore();
		adjustCurrentPrint();
	}
/*	
	private void rotate(Quaternion q){
		this.rotation.mulLeft(q);
		q.toMatrix(helpMatrix.val);
		this.translation.mul(helpMatrix);
		lastPrintModified = currentPrint;
	}
*/	
	public void rotateByPoint(Quaternion q, Vector3 point){
		this.rotation.mulLeft(q);
		q.toMatrix(helpMatrix.val);
		this.translation.sub(point);
		this.translation.mul(helpMatrix);
		translate(point);
	}
	
	public void translate(Vector3 go){
		translate(go.x, go.y, go.z);
//		edit = true;
		lastPrintModified = currentPrint;
	}
	
	public void translate(float x, float y, float z){
		translation.add(x, y, z);
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
		transformMatrix.set(translation, rotation, scale);
	}
	
	/**
	 * For concurrency safe sake use only in owner Motor thread
	 * @return link to transform matrix
	 */
	public Matrix4 linkTransform(){
		return transformMatrix;
	}

	public I print() {
		return (I) new NodeDataPrint(printStore);
	}
}
