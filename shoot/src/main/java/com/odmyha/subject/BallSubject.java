package com.odmyha.subject;

import java.util.Arrays;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.item.ListenDistrictSubject;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.extent.entity.mesh.ModelSubjectPrint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.shoot.Ball;

public class BallSubject extends ListenDistrictSubject<Ball, SubjectPrint> implements RenderableProvider{
	
	private ModelInstance modelInstance;
	private Matrix4 transform = new Matrix4();
	private boolean edit = false;

	public BallSubject(Brick brick) {
		super(brick);
		Point c = brick.getCenter();
		Point one = brick.getPoints().get(0);
		int len = (int) Math.round(VectorHelper.vectorLen(new Ipoint(one.getX() - c.getX(), one.getY() - c.getY())));
		
		modelInstance = ModelStorage.instance().getModelInstance("ball_" + (len * 2));
		BoundingBox bb = modelInstance.nodes.get(0).parts.get(0).meshPart.mesh.calculateBoundingBox();
		
		float f[] = new float[1000 * 8];
		Arrays.fill(f, 0f);
		int oneVertex = modelInstance.nodes.get(0).parts.get(0).meshPart.mesh.getVertexSize();
		modelInstance.nodes.get(0).parts.get(0).meshPart.mesh.getVertices(f);
		float maxY = 0f, minY = 0f;
		for(int i = 1; i < f.length; i += 6){
			if(maxY < f[i]){
				maxY = f[i];
			}
			if(minY > f[i]){
				minY = f[i];
			}
		}
	}
	
	@Override
	public void translate(int x, int y){
		super.translate(x, y);
		synchronized(transform){
			transform.translate((float) x, (float) y, 0f);
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
	
	public Matrix4 getTransform(){
		return this.transform;
	}
}
