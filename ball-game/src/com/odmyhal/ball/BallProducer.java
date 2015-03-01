package com.odmyhal.ball;

import java.util.Collection;
import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.item.Stone;

import com.odmyha.shoot.Ball;
import com.odmyha.weapon.Cannon;
import com.odmyha.weapon.Shield;

public class BallProducer {

	public Ball produceBall(int startX, int startY){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(30, 0));
		points.add(new Ipoint(27, 15));
		points.add(new Ipoint(15, 27));
		points.add(new Ipoint(0, 30));
		points.add(new Ipoint(-15, 27));
		points.add(new Ipoint(-27, 15));
		points.add(new Ipoint(-30, 0));
		points.add(new Ipoint(-27, -15));
		points.add(new Ipoint(-15, -27));
		points.add(new Ipoint(0, -30));
		points.add(new Ipoint(15, -27));
		points.add(new Ipoint(27, -15));
		Brick brick = new PointSetBrick(points);
		Ball res = Ball.create(brick);
		Fpoint vector = res.getVector();
//		vector.setX(373);
//		vector.setY(-393);
		vector.setX(20);
		vector.setY(-50);
		res.translate(startX, startY);
		return res;
	}
	
	public Ball produceBall(int startX, int startY, float vX, float vY){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(30, 0));
		points.add(new Ipoint(27, 15));
		points.add(new Ipoint(15, 27));
		points.add(new Ipoint(0, 30));
		points.add(new Ipoint(-15, 27));
		points.add(new Ipoint(-27, 15));
		points.add(new Ipoint(-30, 0));
		points.add(new Ipoint(-27, -15));
		points.add(new Ipoint(-15, -27));
		points.add(new Ipoint(0, -30));
		points.add(new Ipoint(15, -27));
		points.add(new Ipoint(27, -15));
		Brick brick = new PointSetBrick(points);
		Ball res = Ball.create(brick);
		Fpoint vector = res.getVector();
		vector.setX(vX);
		vector.setY(vY);
		res.translate(startX, startY);
		return res;
	}
	
	public Ball produceSmallBall(int startX, int startY, float vX, float vY){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(15, 0));
		points.add(new Ipoint(12, 9));
		points.add(new Ipoint(9, 12));
		points.add(new Ipoint(0, 15));
		points.add(new Ipoint(-9, 12));
		points.add(new Ipoint(-12, 9));
		points.add(new Ipoint(-15, 0));
		points.add(new Ipoint(-12, -9));
		points.add(new Ipoint(-9, -12));
		points.add(new Ipoint(0, -15));
		points.add(new Ipoint(9, -12));
		points.add(new Ipoint(12, -9));
		Brick brick = new PointSetBrick(points);
		Ball res = Ball.create(brick);
		Fpoint vector = res.getVector();
		vector.setX(vX);
		vector.setY(vY);
		res.translate(startX, startY);
		return res;
	}
	
	public Stone produceShield(int x, int y){
		Shield shield = Shield.instance();
		shield.translate(x, y);
		return shield;
	}
	
	public Cannon produceCannon(int x, int y){
		Cannon cannon = new Cannon();
		cannon.translate(x, y);
		return cannon;
	}
/*	
	public Map<String, String> produceProperties(){
		Map<String, String> props = new HashMap<String, String>();
		props.put("sector.length", "250");
		props.put("buffer.luft", "110");
		props.put("sector.initial.capacity", "20");
		props.put("world.rows.count", "80");
		props.put("world.cols.count", "80");
		props.put("motor.count", "8");
		props.put("event.handel.registrator", "org.bricks.engine.event.handler.EventHandlerRegistrator");
		props.put("model.construct.tool", "org.bricks.enterprise.gen.GenModelConstructTool");
		return props;
	}
	*/
}
