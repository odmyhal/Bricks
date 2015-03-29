package org.bricks.extent.space;

import org.bricks.core.entity.type.Brick;
import org.bricks.engine.staff.Entity;
import org.bricks.extent.subject.model.ModelBrickOperable;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class SpaceSubjectOperable<E extends Entity, I extends SSPrint, M extends ModelBrickOperable> extends SpaceSubject<E, I, M>{

	public SpaceSubjectOperable(ModelInstance ms, String[] operNodes, Vector3... ctr) {
		super(ms, ctr);
		this.modelBrick.initiateNodeOperators(operNodes);
	}

	@Override
	protected M provideModelBrick(ModelInstance modelInstance){
		return (M) new ModelBrickOperable(modelInstance);
	}
}
