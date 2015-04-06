package org.bricks.extent.subject.model;

import java.util.ArrayList;
import java.util.Collection;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.extent.space.overlap.Skeleton;
import org.bricks.extent.space.overlap.SkeletonPrint;

import com.badlogic.gdx.math.Matrix4;

public class MBPrint<P extends ModelBrick<?>> extends BasePrint<P>{

	public Collection<SkeletonPrint> skeletons;
	protected final Matrix4 transformMatrix = new Matrix4();
	protected int lastPrintModified = -6;

	public MBPrint(PrintStore<P, ?> ps) {
		super(ps);
		if(this.getTarget().skeletons != null){
			this.skeletons = new ArrayList<SkeletonPrint>();
		}
	}

//	@Override
	public void init() {
		P target = getTarget();
		if(this.lastPrintModified < target.lastPrintModified){
			transformMatrix.set(target.linkTransform());
			this.lastPrintModified = target.lastPrintModified;
			if(skeletons != null){
				skeletons.clear();
				for(Skeleton<? extends SkeletonPrint> skeleton : target.skeletons){
					skeletons.add(skeleton.getSafePrint());
				}
			}
		}
	}
	
	public Matrix4 linkTransform(){
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
