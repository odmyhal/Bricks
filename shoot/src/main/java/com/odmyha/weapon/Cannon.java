package com.odmyha.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.annotation.EventHandle;
import org.bricks.annotation.OverlapCheck;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.BrickOverlapAlgorithm;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.event.overlap.SmallEventStrategy;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Origin2D;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.entity.mesh.ModelSubjectSync;
import org.bricks.extent.event.ExtentEventGroups;
import org.bricks.extent.event.FireEvent;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.shoot.Ball;

public class Cannon extends MultiRoller<ModelSubjectSync<?, ?>, RollPrint, Fpoint, Roll> implements RenderableProvider{
	
	public static final String CANNON_SOURCE = "CannonSource@shoot.odmyha.com";
	
	private Origin<Fpoint> tmpFire = this.provideInitialOrigin();

	public Cannon() {
		this.addSubject(produceOne());
		this.addSubject(produceTwo());
		registerEventChecker(OverlapChecker.instance());
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(ModelSubjectSync subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}
	
	private ModelSubjectSync<Cannon, EntityPointsPrint> produceOne(){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(150, 175));
		points.add(new Ipoint(125, 200));
		points.add(new Ipoint(25, 200));
		points.add(new Ipoint(0, 175));
		points.add(new Ipoint(0, 100));
		points.add(new Ipoint(50, 50));
		points.add(new Ipoint(100, 50));
		points.add(new Ipoint(150, 100));
		double sin = Math.sin(-Math.PI / 2);
		double cos = Math.cos(-Math.PI / 2);
		for(Ipoint p: points){
			p.setX(p.getX() - 75);
			p.setY(p.getY() - 125);
			PointHelper.rotatePointByZero(p, sin, cos, p);
		}
		Brick brick = new PointSetBrick(points);
//		brick.rotate((float)(-Math.PI / 2), new Ipoint(0, 0));
		ModelInstance tower = ModelStorage.instance().getModelInstance("tower");//ModelProvider.produceTowerModel();//
		return new ModelSubjectSync<Cannon, EntityPointsPrint>(brick, tower);
	}
	
	private ModelSubjectSync<Cannon, EntityPointsPrint> produceTwo(){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(100, 300));
		points.add(new Ipoint(50, 300));
		points.add(new Ipoint(50, 200));
		points.add(new Ipoint(100, 200));
		double sin = Math.sin(-Math.PI / 2);
		double cos = Math.cos(-Math.PI / 2);
		for(Ipoint p: points){
			p.setX(p.getX() - 75);
			p.setY(p.getY() - 125);
			PointHelper.rotatePointByZero(p, sin, cos, p);
		}
		Brick brick = new PointSetBrick(points);
//		brick.rotate((float)(-Math.PI / 2), new Ipoint(0, 0));
		ModelInstance tube = ModelStorage.instance().getModelInstance("tube");//ModelProvider.produceTubeModel()
		return new ModelSubjectSync<Cannon, EntityPointsPrint>(brick, tube);
	}
	
	private void fire(){
		EntityPointsPrint<?, RollPrint> gunView = (EntityPointsPrint<?, RollPrint>) this.getStaff().get(1).getInnerPrint();// getStaff().get(1).getInnerPrint();
/*		List<? extends Point> gunPoints = gunView.getPoints();
		Point one = gunPoints.get(0);
		Point two = gunPoints.get(1);*/
		List<Ipoint> gunPoints = (List<Ipoint>) gunView.getPoints();
		Ipoint one = gunPoints.get(0);
		Ipoint two = gunPoints.get(1);
/*		Ipoint firePoint = new Ipoint((int) Math.round((one.getX() + two.getX()) / 2), 
				(int) Math.round((one.getY() + two.getY()) / 2) );*/
		tmpFire.source.x = (int) Math.round((one.getX() + two.getX()) / 2);
		tmpFire.source.y = (int) Math.round((one.getY() + two.getY()) / 2);
		Ipoint v = new Ipoint(two.getX() - tmpFire.source.getX(), two.getY() - tmpFire.source.getY());
		PointHelper.rotatePointByZero(v, 1, 0, v);
//		firePoint.translate(v.getX(), v.getY());
		tmpFire.source.x += v.getX();
		tmpFire.source.y += v.getY();
		Bullet bullet = Bullet.produce(this);

//		System.out.println("Bullet set rotation to " + gunView.entityPrint.getRotation());
		bullet.setRotation(gunView.entityPrint.getRotation());
		bullet.applyRotation();
		bullet.translate(tmpFire);
		bullet.applyEngine(this.getEngine());
	}

	public String sourceType() {
		return CANNON_SOURCE;
	}
/*
	public Map<String, OverlapStrategy> initOverlapStrategy() {
		BrickOverlapAlgorithm algorithm = new BrickOverlapAlgorithm();
		OverlapStrategy trueStrategy = new OverlapStrategy.TrueOverlapStrategy(algorithm);
		Map<String, OverlapStrategy> cannonStrategy = new HashMap<String, OverlapStrategy>();
		cannonStrategy.put(Cannon.CANNON_SOURCE, new SmallEventStrategy(algorithm));
		cannonStrategy.put(Shield.SHIELD_SOURCE, trueStrategy);
		return cannonStrategy;
	}
*/	
	public Origin<Fpoint> provideInitialOrigin(){
		return new Origin2D();
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Cannon.CANNON_SOURCE, strategyClass = SmallEventStrategy.class)
	@EventHandle(eventType = Cannon.CANNON_SOURCE)
	public void hitCannon(PrintOverlapEvent e){
		this.rollBack(e.getEventTime());
		this.removeHistory(BaseEvent.touchEventCode);
	}
	
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void doNothing(PrintOverlapEvent e){}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Shield.SHIELD_SOURCE, strategyClass = OverlapStrategy.TrueOverlapStrategy.class)
	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void faceShield(PrintOverlapEvent e){
		this.rollBack(e.getEventTime());
		this.removeHistory(BaseEvent.touchEventCode);
	}
/*	
	@EventHandle(eventType = ExtentEventGroups.USER_SOURCE_TYPE)
	public void changeRotationSpeed(RotationSpeedEvent e){
//		System.out.println("Cannon: set rotation speed to " + e.getRotationSpeed());
		this.setRotationSpeed(e.getRotationSpeed());
		flushTimer(e.getEventTime());
	}
*/	
	@EventHandle(eventType = ExtentEventGroups.USER_SOURCE_TYPE)
	public void shoot(FireEvent e){
		this.fire();
	}

	@Override
	protected Roll initializeRoll() {
		return new Roll();
	}
}
