package org.bricks.extent.subject.model;

import java.util.ArrayList;
import java.util.Collection;

import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.tool.Logger;
import org.bricks.engine.tool.Origin;
import org.bricks.exception.Validate;
import org.bricks.extent.space.Roll3D;
import org.bricks.extent.space.SSPrint;
import org.bricks.extent.space.overlap.Skeleton;
import org.bricks.extent.tool.ModelHelper;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelBrick<I extends MBPrint> extends PrintableBase<I> implements RenderableProvider{
	
	public Collection<Skeleton> skeletons;
	protected ModelInstance modelInstance;
	protected Matrix4 transformMatrix = new Matrix4();
	
	private volatile int currentPrintVolatile = -1;
	private int lastPrint = -2, currentPrint = -3, renderPrintModified = -5;
	protected int lastPrintModified = -4;
	
	private final Quaternion tmpQ = new Quaternion();
	private final Matrix4 tmpM = new Matrix4();
	
	public ModelBrick(ModelInstance ms){
		this.modelInstance = ms;
		this.transformMatrix.set(ms.transform);
		this.initPrintStore();
//		this.adjustCurrentPrint();
	}
	
	public void translate(Vector3 v){
		this.translate(v.x, v.y, v.z);
	}
	
	public void translate(float x, float y, float z) {
		transformMatrix.trn(x, y, z);
		lastPrintModified = currentPrint;
	}

	public void rotate(Vector3 spin, float rad, Vector3 central) {
		this.rotate(spin, rad, central.x, central.y, central.z);
	}
	
//	public Logger logger = new Logger();
	public void rotate(Vector3 spin, float rad, float centerX, float centerY, float centerZ) {
//		logger.clearLog();
//		logger.log(" MB>> Matrix before: \n" + transformMatrix);
//		logger.log(String.format("  MB>> rotating spin: %s, rad=%.4f, center: %.4f, %.4f, %.4f", spin, rad, centerX, centerY, centerZ));
		tmpQ.setFromAxisRad(spin, rad);
//		logger.log("  MB>> Quaternion: " + tmpQ);
		tmpQ.toMatrix(tmpM.val);
//		logger.log("  MB>> Translation matrix: \n" + tmpM);
		transformMatrix.trn(-centerX, -centerY, -centerZ);
//		transformMatrix.mulLeft(tmpM);
		ModelHelper.mmultLeft(tmpM, transformMatrix);
		transformMatrix.trn(centerX, centerY, centerZ);
//		logger.log(" MB>> Matrix after: \n" + transformMatrix);
		lastPrintModified = currentPrint;
	}
	
	public Skeleton initSkeleton(Vector3[] points, int[] indexes){
		Skeleton skeleton = new Skeleton(indexes, points);
		skeleton.addTransform(transformMatrix);
		if(skeletons == null){
			skeletons = new ArrayList<Skeleton>();
		}
		skeletons.add(skeleton);
		return skeleton;
	}

	public I print() {
		return (I) new MBPrint(this.printStore);
	}

	public Matrix4 linkTransform(){
		return transformMatrix;
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		int tmpCurPrint = currentPrintVolatile;
		if(!isLastPrint(lastPrint)){
			I mbp = getSafePrint();
			updateModelTransform(mbp);
			mbp.free();
			lastPrint = tmpCurPrint;
		}
		modelInstance.getRenderables(renderables, pool);
	}
	
	protected void updateModelTransform(I modelBrickPrint){
		Validate.isFalse(renderPrintModified > modelBrickPrint.lastPrintModified, "printModified can't be less than render last modified");
		if(renderPrintModified < modelBrickPrint.lastPrintModified){
			modelInstance.transform.set(modelBrickPrint.transformMatrix);
			renderPrintModified = modelBrickPrint.lastPrintModified;
		}
	}
	
	@Override
	public int adjustCurrentPrint(){
		if(skeletons != null && lastPrintModified == currentPrint){
			for(Skeleton skeleton : skeletons){
				skeleton.calculateTransforms();
				skeleton.adjustCurrentPrint();
			}
		}
		currentPrint = super.adjustCurrentPrint();
		currentPrintVolatile = currentPrint;
		return currentPrint;
	}
}
