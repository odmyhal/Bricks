package org.bricks.extent.space;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;
import org.bricks.exception.Validate;
import org.bricks.extent.subject.model.ModelBrick;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import org.bricks.engine.item.MultiLiver;

public class SpaceSubject<E extends Entity, I extends SSPrint> extends Subject<E, I, Vector3, Roll3D> implements RenderableProvider{

	protected ModelBrick modelBrick;
	public final MarkPoint markPoint;
	private final Ipoint monitorCenter = new Ipoint(0, 0);
	
	/**
	 * First point of "ctr" should be center processed be SectorMonitor
	 */
	public SpaceSubject(ModelInstance ms, Vector3...ctr){
		modelBrick = new ModelBrick(ms);
		modelBrick.adjustCurrentPrint();
		Validate.isTrue(ctr.length > 0, "At least central point should exists");
		markPoint = new MarkPoint(ctr);
		markPoint.addTransform(modelBrick.linkTransform());
	}

	public void translate(Origin<Vector3> vector) {
		modelBrick.translate(vector.source);
	}

	public void rotate(Roll3D roll, Origin<Vector3> central) {
		modelBrick.rotate(roll.getSpin(), roll.lastRotation(), central.source);
	}

	public I print() {
		return (I) new SSPrint(printStore);
	}

	@Override
	public Point getCenter() {
		return monitorCenter;
	}

	@Override
	public void update() {
		markPoint.calculateTransforms();
		Vector3 center = markPoint.getMark(0);
		monitorCenter.setX((int)center.x);
		monitorCenter.setY((int)center.y);
		super.update();
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		modelBrick.getRenderables(renderables, pool);
	}

	public int adjustCurrentPrint(){
		super.adjustCurrentPrint();
		return modelBrick.adjustCurrentPrint();
	}
}
