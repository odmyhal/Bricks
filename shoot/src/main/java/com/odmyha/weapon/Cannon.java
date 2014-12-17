package com.odmyha.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.annotation.EventHandle;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.event.overlap.SmallEventStrategy;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.view.RollView;
import org.bricks.engine.view.SubjectView;
import org.bricks.extent.entity.subject.ModelSubject;
import org.bricks.extent.event.ExtentEventGroups;
import org.bricks.extent.event.FireEvent;
import org.bricks.engine.event.RotationSpeedEvent;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.shoot.Ball;

public class Cannon extends MultiRoller<ModelSubject> implements RenderableProvider{
	
	public static final String CANNON_SOURCE = "CannonSource@shoot.odmyha.com";

	public Cannon() {
		this.addSubject(produceOne());
		this.addSubject(produceTwo());
		registerEventChecker(OverlapChecker.instance());
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(ModelSubject subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}
	
	private ModelSubject<Cannon> produceOne(){
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
		return new ModelSubject<Cannon>(brick, tower);
	}
	
	private ModelSubject<Cannon> produceTwo(){
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
		return new ModelSubject<Cannon>(brick, tube);
	}
	
	private void fire(){
		SubjectView<Walker, RollView> gunView = getStaff().get(1).getCurrentView();
		List<Ipoint> gunPoints = gunView.getPoints();
		Ipoint one = gunPoints.get(0);
		Ipoint two = gunPoints.get(1);
		Ipoint firePoint = new Ipoint((int) Math.round((one.getX() + two.getX()) / 2), 
				(int) Math.round((one.getY() + two.getY()) / 2) );
		Ipoint v = new Ipoint(two.getX() - firePoint.getX(), two.getY() - firePoint.getY());
		PointHelper.rotatePointByZero(v, 1, 0, v);
		firePoint.translate(v.getX(), v.getY());
		Bullet bullet = Bullet.produce();
		
		bullet.setRotation(gunView.getEntityView().getRotation());
		bullet.applyRotation();
		bullet.translate(firePoint.getX(), firePoint.getY());
		bullet.applyEngine(this.getEngine());
	}

	public String sourceType() {
		return CANNON_SOURCE;
	}

	public Map<String, OverlapStrategy> initOverlapStrategy() {
		Map<String, OverlapStrategy> cannonStrategy = new HashMap<String, OverlapStrategy>();
		cannonStrategy.put(Cannon.CANNON_SOURCE, new SmallEventStrategy(this));
		cannonStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
		return cannonStrategy;
	}
	
	@EventHandle(eventType = Cannon.CANNON_SOURCE)
	public void hitCannon(OverlapEvent e){
		this.rollBack(e.getEventTime());
		this.removeHistory(BaseEvent.touchEventCode);
	}
	
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void doNothing(OverlapEvent e){}
	
	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void faceShield(OverlapEvent e){
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
}
