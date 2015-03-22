package com.odmyha.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.annotation.EventHandle;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.item.MultiWalkRoller;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.tool.Origin2D;
import org.bricks.extent.entity.mesh.ModelSubject;
import org.bricks.extent.entity.mesh.ModelSubjectPrint;
import org.bricks.extent.entity.mesh.ModelSubjectSync;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.shoot.Ball;

public class Bullet extends MultiWalkRoller<ModelSubjectSync<?, ?>, WalkPrint> implements RenderableProvider{
	
	public static final String BULLET_SOURCE = "BulletSource@shoot.odmyha.com";
	private static final float speed = 1000;
	
	private static final Map<String, OverlapStrategy> bulletStrategy = new HashMap<String, OverlapStrategy>();
	static{
		bulletStrategy.put(Ball.BALL_SOURCE_TYPE, OverlapStrategy.TRUE);
		bulletStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
	}
	
	private Bullet(ModelSubjectSync<Bullet, SubjectPrint> s){
		addSubject(s);
		registerEventChecker(OverlapChecker.instance());
		setVector(new Origin2D(new Fpoint(speed, 0f)));
	}
	
	public static Bullet produce(){
		return new Bullet(produceSubject());
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(ModelSubjectSync subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}
	
	private static ModelSubjectSync<Bullet, SubjectPrint> produceSubject(){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(0, 0));
		points.add(new Ipoint(25, 0));
		points.add(new Ipoint(25, 27));
		points.add(new Ipoint(13, 40));
		points.add(new Ipoint(0, 27));
		double sin = Math.sin(-Math.PI / 2);
		double cos = Math.cos(-Math.PI / 2);
		for(Ipoint p: points){
			p.setX(p.getX() - 12);
			p.setY(p.getY() - 13);
			PointHelper.rotatePointByZero(p, sin, cos, p);
		}
		Brick brick = new PointSetBrick(points);
		ModelInstance bulletModel = ModelStorage.instance().getModelInstance("bullet");//ModelStorage.instance().getModelInstance("gilse", "cone");//
		return new ModelSubjectSync<Bullet, SubjectPrint>(brick, bulletModel);
	}
	
	@Override
	public void setRotation(float radians){
		double dr = radians;
		Fpoint mVector = getVector().source;
		mVector.setX((float)Math.cos(dr));
		mVector.setY((float)Math.sin(dr));
		PointHelper.normalize(mVector, speed);
		super.setRotation(radians);
	}

	public String sourceType() {
		return BULLET_SOURCE;
	}

	public Map<String, OverlapStrategy> initOverlapStrategy() {
		return bulletStrategy;
	}

	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void faceShield(OverlapEvent e){
		this.outOfWorld();
	}
	
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void faceBall(OverlapEvent e){
		this.outOfWorld();
	}

}
