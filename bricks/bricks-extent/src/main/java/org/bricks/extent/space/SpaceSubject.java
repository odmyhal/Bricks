package org.bricks.extent.space;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SpaceSubject<E extends Entity, I extends SSPrint> extends Subject<E, I, Vector3, Roll3D> implements RenderableProvider{
	
	private ModelInstance modelInstance;
	private final Matrix4 localMatrix = new Matrix4().idt();
	private final Matrix4 transformMatrix = new Matrix4().idt();
	protected final MarkPoint markPoint;
	private final Ipoint monitorCenter = new Ipoint(0, 0);
	
	private final Quaternion tmpQ = new Quaternion();
	private final Matrix4 tmpM = new Matrix4();
	
	
	private volatile int currentPrintVolatile = -1;
	private int lastPrint = -2, currentPrint = -3, renderPrintModified = -5;
	protected int lastPrintModified = -4;
	
	/**
	 * First point of "ctr" should be center processed be SectorMonitor
	 */
	public SpaceSubject(ModelInstance ms, Vector3...ctr){
		modelInstance = ms;
		initLocalMatrix(ms.nodes);
		transformMatrix.set(ms.transform);
		Validate.isTrue(ctr.length > 0, "At least central point should exists");
		markPoint = new MarkPoint(ctr);
		markPoint.addTransform(localMatrix);
		markPoint.addTransform(transformMatrix);
	}
	
	private void initLocalMatrix(Array<Node> nodes){
		Validate.isTrue(nodes.size == 1, "SpaceSubject is constructed to manage only one node...");
		for(Node node : nodes){
			localMatrix.mulLeft(node.globalTransform);
			if(node.children.size > 0){
				initLocalMatrix(node.children);
			}
		}
	}

	public void translate(Origin<Vector3> vector) {
		transformMatrix.trn(vector.source);
		lastPrintModified = currentPrint;
	}

	public void rotate(Roll3D roll, Origin<Vector3> central) {
		tmpQ.setFromAxisRad(roll.getSpin(), roll.lastRotation());
		tmpQ.toMatrix(tmpM.val);
		transformMatrix.trn(-central.source.x, -central.source.y, -central.source.z);
		transformMatrix.mulLeft(tmpM);
		transformMatrix.trn(central.source);
		lastPrintModified = currentPrint;
	}

	public I print() {
		return (I) new SSPrint(printStore);
	}

	@Override
	public Point getCenter() {
		return monitorCenter;
	}

	public void update() {
		markPoint.calculateTransforms();
		Vector3 center = markPoint.getMark(0);
		monitorCenter.setX((int)center.x);
		monitorCenter.setY((int)center.y);
		super.update();
	}
	
	public Matrix4 linkTransform(){
		return transformMatrix;
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		int tmpCurPrint = currentPrintVolatile;
		if(!isLastPrint(lastPrint)){
			SSPrint<?, ?> ssp = getSafePrint();
			Validate.isFalse(renderPrintModified > ssp.lastPrintModified, "printModified can't be less than render last modified");
			if(renderPrintModified < ssp.lastPrintModified){
				modelInstance.transform.set(ssp.transform);
				renderPrintModified = ssp.lastPrintModified;
			}
			ssp.free();
			lastPrint = tmpCurPrint;
		}
		modelInstance.getRenderables(renderables, pool);
	}
	
	@Override
	public int adjustCurrentPrint(){
		currentPrint = super.adjustCurrentPrint();
		currentPrintVolatile = currentPrint;
		return currentPrint;
	}
}
