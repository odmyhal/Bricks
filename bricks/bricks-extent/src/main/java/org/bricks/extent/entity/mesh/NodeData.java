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
		initPrintStore();
		adjustCurrentPrint();
//		printStore = new PrintStore(this);
//		adjustCurrentView();
	}
	
	private void setRotation(Quaternion quaternion){
		this.rotation.set(quaternion);
	}
	
	private void setTranslation(Vector3 translation){
		this.translation.set(translation);
	}

	public void rotate(Quaternion q){
		this.rotation.mul(q);
		q.toMatrix(helpMatrix.val);
		this.translation.mul(helpMatrix);
//		edit = true;
		lastPrintModified = currentPrint;
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
/*	
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
	*/

	@Override
	public int adjustCurrentPrint() {
		currentPrint = printStore.adjustCurrentPrint();
//		edit = false;
		return currentPrint;
	}

	public I print() {
		return (I) new NodeDataPrint(printStore);
	}
}
