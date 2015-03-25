package org.bricks.extent.entity.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.staff.Entity;
import org.bricks.exception.Validate;
import org.bricks.extent.subject.model.ModelBrick;
import org.bricks.extent.subject.model.ModelBrickOperable;
import org.bricks.extent.subject.model.NodeData;
import org.bricks.extent.subject.model.NodeDataPrint;
import org.bricks.extent.subject.model.NodeOperator;
import org.bricks.extent.tool.ModelHelper;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ModelSubjectOperable<E extends Entity, I extends ModelSubjectPrint, M extends ModelBrickOperable> extends ModelSubject<E, I, M>{
	
	public ModelSubjectOperable(Brick brick, ModelInstance modelInstance, String... operNodes){
		super(brick, modelInstance);
		this.modelBrick.initiateNodeOperators(operNodes);
	}
	
	protected M provideModelBrick(ModelInstance modelInstance){
		return (M) new ModelBrickOperable(modelInstance);
	}
	
}
