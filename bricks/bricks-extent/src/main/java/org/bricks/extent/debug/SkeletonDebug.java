package org.bricks.extent.debug;

import org.bricks.extent.subject.model.MBPrint;
import org.bricks.extent.subject.model.ModelBrick;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SkeletonDebug implements RenderableProvider{

	private ModelInstance modelInstance;
	private ModelBrick<?> modelBrick;
	private int printModified = -7;
	
	public SkeletonDebug(ModelInstance modelInstance, ModelBrick<?> modelBrick){
		this.modelInstance = modelInstance;
		this.modelBrick = modelBrick;
	}

	public void getRenderables(Array<Renderable> renderables,
			Pool<Renderable> pool) {
		MBPrint mbPrint = modelBrick.getSafePrint();
		if(printModified < mbPrint.lastPrintModified()){
			modelInstance.transform.set(mbPrint.linkTransform());
			printModified = mbPrint.lastPrintModified();
		}
		mbPrint.free();
		modelInstance.getRenderables(renderables, pool);
	}
}
