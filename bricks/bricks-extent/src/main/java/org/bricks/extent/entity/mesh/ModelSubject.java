package org.bricks.extent.entity.mesh;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.subject.model.ModelBrick;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelSubject<E extends Entity, I extends ModelSubjectPrint, M extends ModelBrick> extends BrickSubject<E, I> implements RenderableProvider{

	public M modelBrick;
	private static final Vector3 spin = new Vector3(0f, 0f, 99f);
	
	public ModelSubject(Brick brick, ModelInstance modelInstance){
		super(brick);
		this.modelBrick = provideModelBrick(modelInstance);
		this.modelBrick.adjustCurrentPrint();
	}
	
	protected M provideModelBrick(ModelInstance modelInstance){
		return (M) new ModelBrick(modelInstance);
	}
	
	public void translate(Origin<Fpoint> origin){
		super.translate(origin.source);
		modelBrick.translate(origin.source.x, origin.source.y, 0f);
	}
	
	public void rotate(Roll roll, Origin<Fpoint> central){
		super.rotate(roll.getRotation(), central.source);
		modelBrick.rotate(spin, roll.lastRotation(), central.source.x, central.source.y, 0f);
	}
/*	
	@Override
	public void translate(int x, int y){
		super.translate(x, y);
		modelBrick.translate(x, y, 0f);
	}
	
	
/*	
	@Override
	public void rotate(float rad, Point central){
		super.rotate(rad, central);
		modelBrick.rotate(spin, rad, central.getX(), central.getY(), 0f);
	}
*/	
	@Override
	public I print(){
		return (I) new ModelSubjectPrint(this.printStore);
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		modelBrick.getRenderables(renderables, pool);
	}
/*	
	@Override
	public void update() {
		super.update();
		modelBrick.adjustCurrentPrint();
	}
	*/
	
	public int adjustCurrentPrint(){
		int res = modelBrick.adjustCurrentPrint();
		super.adjustCurrentPrint();
		return res;
	}
}
