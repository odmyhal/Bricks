package org.bricks.extent.space;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.neve.PrintFactory;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.exception.Validate;
import org.bricks.extent.space.overlap.MarkPoint;
import org.bricks.extent.subject.model.MBSVehicle;
import org.bricks.extent.subject.model.ModelBrick;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SpaceSubject<E extends Entity, I extends SSPrint, C, R extends Roll, M extends ModelBrick> 
//	extends BaseSubject<E, I, Vector3, Roll3D> 
	extends BaseSubject<E, I, C, R>
	implements RenderableProvider, ModelBrickSubject<E, I, C, R, M>{

	public M modelBrick;
	public final MarkPoint markPoint;
	private final Ipoint monitorCenter = new Ipoint(0, 0);
	private SpaceVehicle<C, R> vehicle;
	private PrintFactory<I> printFactory = (PrintFactory<I>) SSPrint.printFactory;
	
	/**
	 * First point of "ctr" should be center processed be SectorMonitor
	 */
	public SpaceSubject(MBSVehicle<C, R> vehicle, ModelInstance ms, Vector3...ctr){
		modelBrick = provideModelBrick(ms);
		modelBrick.adjustCurrentPrint();
		Validate.isTrue(ctr.length > 0, "At least central point should exists");
		markPoint = new MarkPoint(ctr);
		markPoint.addTransform(modelBrick.linkTransform());
		
		vehicle.setModelBrick(modelBrick);
		this.vehicle = vehicle;
	}
	
	protected M provideModelBrick(ModelInstance modelInstance){
		return (M) new ModelBrick(modelInstance);
	}
	
	public M linkModelBrick(){
		return modelBrick;
	}
	
	public void translate(Origin<C> vector) {
		vehicle.translate(vector);
	}

	public void rotate(R roll, Origin<C> central) {
		vehicle.rotate(roll, central);
	}
	
/*
	public void translate(Origin<Vector3> vector) {
		modelBrick.translate(vector.source);
	}

	public void rotate(Roll3D roll, Origin<Vector3> central) {
		modelBrick.rotate(roll.getSpin(), roll.lastRotation(), central.source);
	}
*/	
	public void setPrintFactory(PrintFactory<I> pf){
		this.printFactory = pf;
	}

	public I print() {
		return printFactory.producePrint(printStore);
//		return (I) new SSPrint(printStore);
	}

	@Override
	public Point getCenter() {
		return monitorCenter;
	}
	
	private void updateCenter(){
		markPoint.calculateTransforms();
		Vector3 center = markPoint.getMark(0);
		monitorCenter.setX((int)center.x);
		monitorCenter.setY((int)center.y);
	}

	@Override
	public void update() {
		updateCenter();
		super.update();
	}

	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		modelBrick.getRenderables(renderables, pool);
	}

	public int adjustCurrentPrint(){
		int res = modelBrick.adjustCurrentPrint();
		super.adjustCurrentPrint();
		return res;
	}
	
	public void joinWorld(World world){
		updateCenter();
		super.joinWorld(world);
	}
}
