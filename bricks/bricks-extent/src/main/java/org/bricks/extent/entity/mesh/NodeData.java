package org.bricks.extent.entity.mesh;

import java.util.LinkedList;

import org.bricks.engine.view.EntityView;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeData {
	

	private NodeDataView view;
	private LinkedList<NodeDataView> viewCache = new LinkedList<NodeDataView>();
	
	public final Quaternion rotation = new Quaternion();
	public final Vector3 translation = new Vector3();
	
	private Matrix4 helpMatrix = new Matrix4();
	private Vector3 helpPoint = new Vector3();
	
	public NodeData(Node node){
		this(node.rotation, node.translation);
	}
	
	public NodeData(Quaternion rotation, Vector3 translation){
		setRotation(rotation);
		setTranslation(translation);
		adjustCurrentView();
	}
	
	public void setRotation(Quaternion quaternion){
		this.rotation.set(quaternion);
	}
	
	public void setTranslation(Vector3 translation){
		this.translation.set(translation);
	}

	public void rotate(Quaternion q){
		this.rotation.mul(q);
		q.toMatrix(helpMatrix.val);
		this.translation.mul(helpMatrix);
	}
	
	public void rotateByPoint(Quaternion q, Vector3 point){
		this.rotation.mul(q);
		q.toMatrix(helpMatrix.val);
		this.translation.mul(helpMatrix);
		this.helpPoint.set(point);
		this.helpPoint.mul(helpMatrix);
		this.helpPoint.sub(point).scl(-1);
		translate(this.helpPoint);
	}
	
	public void translate(Vector3 go){
		translate(go.x, go.y, go.z);
	}
	
	public void translate(float x, float y, float z){
		translation.add(x, y, z);
	}
	
	protected LinkedList viewCache(){
		return this.viewCache;
	}
	
	//TODO need refuse of synchronization use Concurrent collection and volatile modificator for this.view	
	protected final void adjustCurrentView(){
		synchronized(viewCache){
			NodeDataView nView = viewCache.pollFirst();
			if(nView == null){
				nView = this.provideCurrentView();
			}
			nView.init();
			nView.occupy();
			if(this.view != null){
				this.view.free();
			}
			this.view = nView;
		}
	}
	
	protected NodeDataView provideCurrentView(){
		return new NodeDataView(this);
	}
	
	public NodeDataView getCurrentView(){
		synchronized(viewCache){
			this.view.occupy();
			return this.view;
		}
	}
}
