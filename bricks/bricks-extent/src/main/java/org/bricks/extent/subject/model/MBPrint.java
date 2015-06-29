package org.bricks.extent.subject.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.exception.Validate;
import org.bricks.extent.rewrite.Matrix4Safe;
import org.bricks.extent.space.overlap.MarkPoint;
import org.bricks.extent.space.overlap.Skeleton;
import org.bricks.extent.space.overlap.SkeletonPlanePrint;
import org.bricks.extent.space.overlap.SkeletonPrint;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MBPrint<P extends ModelBrick<?>> extends BasePrint<P>{

	public List<SkeletonPrint> skeletons;
	private Vector3[] marks;
	
	protected final Matrix4Safe transformMatrix = new Matrix4Safe();
	protected int lastPrintModified = -6;
	
	private int planeSkeletonPrint = -1;

	public MBPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	public void init() {
		P target = getTarget();
		if(this.lastPrintModified < target.lastPrintModified){
			transformMatrix.set(target.linkTransform());
			this.lastPrintModified = target.lastPrintModified;
			if(this.planeSkeletonPrint != target.planeSkeleton){
				this.planeSkeletonPrint = target.planeSkeleton;
			}
		}
		if(target.skeletons != null){
			if(skeletons == null){
				skeletons = new ArrayList<SkeletonPrint>();
			}else{
				skeletons.clear();
			}
			for(Skeleton<? extends SkeletonPrint> skeleton : target.skeletons){
				skeletons.add(skeleton.getSafePrint());
			}
		}
		if(marks == null){
			marks = new Vector3[target.markPoint.size];
			for(int i = 0; i < marks.length; i++){
				marks[i] = new Vector3();
			}
		}
		for(int i = 0; i < marks.length; i++){
			marks[i].set(target.markPoint.getMark(i));
		}
	}
	
	public Vector3 getCenter(){
		return marks[0];
	}
	
	public Vector3 getMark(int index){
		return marks[index];
	}
	
	public SkeletonPlanePrint getSkeletonPlanePrint(){
		Validate.isTrue(planeSkeletonPrint > -1, "MBPrint has not initialized plane Skeleton whith ModelBrick.applySkeletonWithPlane");
		return (SkeletonPlanePrint) skeletons.get(planeSkeletonPrint);
	}
	
	public Matrix4Safe linkTransform(){
		return transformMatrix;
	}
	
	public int lastPrintModified(){
		return lastPrintModified;
	}
	
	protected void endUse(){
		if(skeletons != null){
			for(SkeletonPrint sp : skeletons){
				sp.free();
			}
		}
		super.endUse();
	}

}
