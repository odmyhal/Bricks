package org.bricks.extent.subject.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.exception.Validate;
import org.bricks.extent.rewrite.Matrix4Safe;
import org.bricks.extent.space.overlap.Skeleton;
import org.bricks.extent.space.overlap.SkeletonPlanePrint;
import org.bricks.extent.space.overlap.SkeletonPrint;

import com.badlogic.gdx.math.Matrix4;

public class MBPrint<P extends ModelBrick<?>> extends BasePrint<P>{

	public List<SkeletonPrint> skeletons;
	protected final Matrix4Safe transformMatrix = new Matrix4Safe();
	protected int lastPrintModified = -6;
	
	private int planeSkeletonPrint = -1;

	public MBPrint(PrintStore<P, ?> ps) {
		super(ps);
/*		if(this.getTarget().skeletons != null){
			this.skeletons = new ArrayList<SkeletonPrint>();
		}*/
	}

//	@Override
	public void init() {
		P target = getTarget();
//		System.out.println("MBPrint initializing mypl " + this.planeSkeletonPrint + ", targetpl " + target.planeSkeleton);
//		System.out.println("MBPrint initializing mylastprint " + this.lastPrintModified + ", targetlpr " + target.lastPrintModified);
		if(this.lastPrintModified < target.lastPrintModified){
			transformMatrix.set(target.linkTransform());
			this.lastPrintModified = target.lastPrintModified;
			if(this.planeSkeletonPrint != target.planeSkeleton){
				this.planeSkeletonPrint = target.planeSkeleton;
//				System.out.println("New planeSkeletonPrint for " + this + " is " + this.planeSkeletonPrint);
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
	}
/*	
	public void setPlaneSkeletonPrint(int skeletonNumber){
		planeSkeletonPrint = skeletonNumber;
	}
*/	
	public SkeletonPlanePrint getSkeletonPlanePrint(){
//		System.out.println("Found planeSkeletonPrint for " + this + " is " + this.planeSkeletonPrint);
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
