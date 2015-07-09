package com.odmyha.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.annotation.EventHandle;
import org.bricks.annotation.OverlapCheck;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.BrickOverlapAlgorithm;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.item.MultiWalkRoller2D;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin2D;
import org.bricks.extent.entity.mesh.ModelSubjectSync;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.shoot.Ball;

public class Bullet extends MultiWalkRoller2D<ModelSubjectSync<?, ?>, WalkPrint> implements RenderableProvider{
	
	public static final String BULLET_SOURCE = "BulletSource@shoot.odmyha.com";
	private static final float speed = 1000;
	
	private Cannon gun = null;
/*	
	private static final Map<String, OverlapStrategy> bulletStrategy = new HashMap<String, OverlapStrategy>();
	static{
		bulletStrategy.put(Ball.BALL_SOURCE_TYPE, OverlapStrategy.TRUE);
		bulletStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
	}
*/	
	private Bullet(ModelSubjectSync<Bullet, EntityPointsPrint> s, Cannon cannon){
		addSubject(s);
		registerEventChecker(OverlapChecker.instance());
		setVector(new Origin2D(new Fpoint(speed, 0f)));
		this.gun = cannon;
	}
	
	public static Bullet produce(Cannon cannon){
		return new Bullet(produceSubject(), cannon);
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(ModelSubjectSync subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}
	
	private static ModelSubjectSync<Bullet, EntityPointsPrint> produceSubject(){
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
		return new ModelSubjectSync<Bullet, EntityPointsPrint>(brick, bulletModel);
	}
/*	
	@Override
	public void setRotation(float radians){
		double dr = radians;
		Fpoint mVector = getVector().source;
		mVector.setX((float)Math.cos(dr));
		mVector.setY((float)Math.sin(dr));
		PointHelper.normalize(mVector, speed);
		super.setRotation(radians);
	}
*/
	public String sourceType() {
		return BULLET_SOURCE;
	}
/*
	public Map<String, OverlapStrategy> initOverlapStrategy() {
		BrickOverlapAlgorithm algorithm = new BrickOverlapAlgorithm();
		OverlapStrategy trueStrategy = new OverlapStrategy.TrueOverlapStrategy(algorithm);
		Map<String, OverlapStrategy> bulletStrategy = new HashMap<String, OverlapStrategy>();
		bulletStrategy.put(Ball.BALL_SOURCE_TYPE, trueStrategy);
		bulletStrategy.put(Shield.SHIELD_SOURCE, trueStrategy);
		return bulletStrategy;
	}
*/
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Shield.SHIELD_SOURCE, strategyClass = OverlapStrategy.TrueOverlapStrategy.class)
	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void faceShield(PrintOverlapEvent e){
		this.disappear();
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Ball.BALL_SOURCE_TYPE, strategyClass = OverlapStrategy.TrueOverlapStrategy.class)
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void faceBall(PrintOverlapEvent e){
		this.disappear();
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Cannon.CANNON_SOURCE, strategyMethod = "checkCannonOverlap")
	@EventHandle(eventType = Cannon.CANNON_SOURCE)
	public void faceOtherCannon(PrintOverlapEvent e){
		this.disappear();
	}
	
	public boolean checkCannonOverlap(Subject<Cannon, ?, ?, ?> s){
		return s.getEntity() != gun;
	}

}
