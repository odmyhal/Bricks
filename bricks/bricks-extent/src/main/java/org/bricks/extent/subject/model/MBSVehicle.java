package org.bricks.extent.subject.model;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.space.Roll3D;
import org.bricks.extent.space.SpaceVehicle;

import com.badlogic.gdx.math.Vector3;

public abstract class MBSVehicle<C, R extends Roll> implements SpaceVehicle<C, R> {
	
	protected ModelBrick modelBrick;
	
	public void setModelBrick(ModelBrick mb){
		this.modelBrick = mb;
	}
	
	public static class Space extends MBSVehicle<Vector3, Roll3D>{

		public void translate(Origin<Vector3> vector) {
			modelBrick.translate(vector.source);
		}

		public void rotate(Roll3D roll, Origin<Vector3> central) {
			modelBrick.rotate(roll.getSpin(), roll.lastRotation(), central.source);
		}
		
	}
	
	public static class Plane extends MBSVehicle<Fpoint, Roll>{
		
		private static final Vector3 spin = new Vector3(0f, 0f, 99f);

		public void translate(Origin<Fpoint> vector) {
			modelBrick.translate(vector.source.x, vector.source.y, 0f);
		}

		public void rotate(Roll roll, Origin<Fpoint> central) {
			modelBrick.rotate(spin, roll.lastRotation(), central.source.x, central.source.y, 0f);
		}
		
	}
}
