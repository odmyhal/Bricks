package org.bricks.extent.entity.mesh;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.staff.Entity;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelSubjectSync<E extends Entity, I extends SubjectPrint> extends ModelSubjectBase<E, I>{
	
	private boolean edit = true;

	public ModelSubjectSync(Brick brick, ModelInstance modelInstance) {
		super(brick, modelInstance);
	}
/*	
	public ModelSubjectSync(Brick brick, ModelInstance modelInstance, Matrix4 initialTransform){
		super(brick, modelInstance, initialTransform);
	}
*/
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
//				System.out.println("Editing transform " + this.entity.getClass().getCanonicalName());
//				System.out.println("Transform is: " + transform);
			}
		}
		modelInstance.getRenderables(renderables, pool);
	}
	

}
