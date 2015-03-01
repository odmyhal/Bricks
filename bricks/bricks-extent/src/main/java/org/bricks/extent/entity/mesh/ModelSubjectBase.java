package org.bricks.extent.entity.mesh;

import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;

public abstract class ModelSubjectBase<E extends Entity, I extends SubjectPrint> extends Subject<E, I> implements RenderableProvider{

	protected ModelInstance modelInstance;
	protected Matrix4 transform;
//	protected boolean edit = true;
//	private volatile int currentPrint = -1;
//	private int lastPrint = -2;
	
	public ModelSubjectBase(Brick brick, ModelInstance modelInstance){
		this(brick, modelInstance, new Matrix4());
	}

	public ModelSubjectBase(Brick brick, ModelInstance modelInstance, Matrix4 initialTransform){
		super(brick);
		this.modelInstance = modelInstance;
		this.transform = initialTransform;
	}
	
	
	@Override
	public I print(){
		return (I) new SubjectPrint(this.printStore);
	}
}