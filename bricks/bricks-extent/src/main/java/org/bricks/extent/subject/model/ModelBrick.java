package org.bricks.extent.subject.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.tool.Logger;
import org.bricks.engine.tool.Origin;
import org.bricks.exception.Validate;
import org.bricks.extent.rewrite.Matrix4Safe;
import org.bricks.extent.space.Roll3D;
import org.bricks.extent.space.SSPrint;
import org.bricks.extent.space.overlap.Skeleton;
import org.bricks.extent.space.overlap.SkeletonWithPlane;
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
	
	public List<Skeleton> skeletons;
	protected ModelInstance modelInstance;
	protected Matrix4Safe transformMatrix = new Matrix4Safe();
	
	private volatile int currentPrintVolatile = -1;
	private int lastPrint = -2, currentPrint = -3, renderPrintModified = -5;
	protected int lastPrintModified = -4;

	protected int planeSkeleton = -1;
	
	public ModelBrick(ModelInstance ms){
		this.modelInstance = ms;
		this.transformMatrix.set(ms.transform);
		this.initPrintStore();
	}
	
	public void translate(Vector3 v){
		this.translate(v.x, v.y, v.z);
	}
	
	public void reset(){
		transformMatrix.idt();
		lastPrintModified = currentPrint;
	}
	
	public void translate(float x, float y, float z) {
		transformMatrix.trn(x, y, z);
		lastPrintModified = currentPrint;
	}

	public void rotate(Vector3 spin, float rad, Vector3 central) {
		this.rotate(spin, rad, central.x, central.y, central.z);
	}
	
	public void rotate(Vector3 spin, float rad, float centerX, float centerY, float centerZ) {
		Matrix4 tmpM = Matrix4Safe.safeTmpMatrix();
		Quaternion tmpQ = Matrix4Safe.safeTmpQuaternion();
		
		tmpQ.setFromAxisRad(spin, rad);
		tmpQ.toMatrix(tmpM.val);
		transformMatrix.trn(-centerX, -centerY, -centerZ);
		ModelHelper.mmultLeft(tmpM, transformMatrix);
		transformMatrix.trn(centerX, centerY, centerZ);
		lastPrintModified = currentPrint;
	}
	
	public Skeleton initSkeleton(Vector3[] points, int[] indexes){
		Skeleton skeleton = new Skeleton(indexes, points);
		return applySkeleton(skeleton);
	}
	
	public Skeleton applySkeleton(Skeleton skeleton){
		skeleton.addTransform(transformMatrix);
		if(skeletons == null){
			skeletons = new ArrayList<Skeleton>();
		}
		skeletons.add(skeleton);
		return skeleton;
	}
	
	public SkeletonWithPlane applySkeletonWithPlane(SkeletonWithPlane swp){
		swp.addTransform(transformMatrix);
		if(skeletons == null){
			skeletons = new ArrayList<Skeleton>();
		}
		skeletons.add((Skeleton)swp);
		planeSkeleton = skeletons.indexOf(swp);
		lastPrintModified = currentPrint;
		Validate.isTrue(planeSkeleton > -1, "PlaneSkeleton should already exists");
		return swp;
	}

	public I print() {
		MBPrint mbp = new MBPrint(this.printStore);
/*		if(planeSkeleton > -1){
			mbp.setPlaneSkeletonPrint(planeSkeleton);
		}*/
		return (I) mbp;
	}

	public Matrix4Safe linkTransform(){
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
	
	/**
	 * Method used in Render Thread
	 * @param modelBrickPrint
	 */
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
	
	public ModelInstance linkModelInstance(){
		return this.modelInstance;
	}
}
