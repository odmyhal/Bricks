package org.bricks.extent.space;

import org.bricks.engine.staff.Entity;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.subject.model.MBSVehicle;
import org.bricks.extent.subject.model.ModelBrickOperable;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class SpaceSubjectOperable<E extends Entity, I extends SSPrint, C, R extends Roll, M extends ModelBrickOperable> extends SpaceSubject<E, I, C, R, M>{

	public SpaceSubjectOperable(MBSVehicle<C, R> vehicle, ModelInstance ms, String[] operNodes, Vector3... ctr) {
		this(vehicle, ms, ctr);
		this.modelBrick.initiateNodeOperators(operNodes);
	}
	
	public SpaceSubjectOperable(MBSVehicle<C, R> vehicle, ModelInstance ms, Vector3... ctr){
		super(vehicle, ms, ctr);
	}

	@Override
	protected M provideModelBrick(ModelInstance modelInstance){
		return (M) new ModelBrickOperable(modelInstance);
	}
}
