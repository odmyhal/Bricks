package org.bricks.extent.entity.mesh;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.staff.Entity;
import org.bricks.extent.rewrite.Matrix4Safe;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

@Deprecated
public class ModelSubjectSync<E extends Entity, I extends EntityPointsPrint> extends BrickSubject<E, I> implements RenderableProvider{
	
	private boolean edit = true;
	private ModelInstance modelInstance;
	private Matrix4Safe transform = new Matrix4Safe();

	public ModelSubjectSync(Brick brick, ModelInstance modelInstance) {
		super(brick);
		this.modelInstance = modelInstance;
		this.transform.set(modelInstance.transform);
	}

	@Override
	public void translate(int x, int y){
		super.translate(x, y);
		synchronized(transform){
			transform.trn((float) x, (float) y, 0f);
			edit = true;
		}
	}
	
	@Override
	public void rotate(float rad, Point central){
		super.rotate(rad, central);
		synchronized(transform){
			transform.setToRotationRad(0, 0, 1f, rad);
			transform.trn(central.getFX(), central.getFY(), 0f);
			edit = true;
		}
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		synchronized(transform){
			if(edit){
				modelInstance.transform.set(transform);
				edit = false;
			}
		}
		modelInstance.getRenderables(renderables, pool);
	}
	
	@Override
	public I print(){
		return (I) new EntityPointsPrint(this.printStore);
	}
}
