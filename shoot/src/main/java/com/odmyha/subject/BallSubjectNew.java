package com.odmyha.subject;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.pool.District;
import org.bricks.extent.entity.mesh.ModelSubjectSync;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.odmyha.shoot.Ball;

//public class BallSubjectNew extends ModelSubjectOperable<Ball, ModelSubjectPrint> implements RenderableProvider{
public class BallSubjectNew extends ModelSubjectSync<Ball, SubjectPrint> implements RenderableProvider{

	public BallSubjectNew(Brick brick) {
		super(brick, produceModelInstance(brick));
	}
	
	private static ModelInstance produceModelInstance(Brick brick){
		Point c = brick.getCenter();
		Point one = brick.getPoints().get(0);
		int len = (int) Math.round(VectorHelper.vectorLen(new Ipoint(one.getX() - c.getX(), one.getY() - c.getY())));
		
		return ModelStorage.instance().getModelInstance("ball_" + (len * 2));
	}

	@Override
	public boolean joinDistrict(District sector){
		boolean result = super.joinDistrict(sector);
		if(result){
			entity.onDistrictJoin(sector);
		}
		return result;
	}
}