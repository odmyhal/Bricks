package org.bricks.engine;

import java.util.Collection;
import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.help.VectorSwapHelper;
import org.bricks.engine.staff.Subject;
import org.junit.Test;


public class VectorTest {

//	@Test
	public void projectionTest(){
		Point target = new Fpoint(-10, 0);
		Point base = new Fpoint(-4, 2);
		Point projection = VectorHelper.vectorProjection(target, base, new Fpoint());
		System.out.println("Projection is: " + projection);
	}
/*	
	@Test
	public void swapTest(){
		Packet target = produceSmallBall(350, 350, 50f, 50f);
		Packet source = produceBall(200, 200, 150f, 150f);
		target.adjustCurrentView();
		source.adjustCurrentView();
		

		Fpoint sourceAdd = VectorSwapHelper.fetchSwapVector(source.getCurrentView(), target.getCurrentView());
		System.out.println("***************Huge ball munus swap: " + sourceAdd);
		
		Fpoint targetAdd = VectorSwapHelper.fetchSwapVector(target.getCurrentView(), source.getCurrentView());
		System.out.println("***************Small ball minus swap: " + targetAdd);
		
		Fpoint targetVector = target.getVector();
		Fpoint sourceVector = source.getVector();
		double totalLen = VectorHelper.vectorLen(targetVector)*target.getWeight() + VectorHelper.vectorLen(sourceVector)*source.getWeight();
		System.out.println(String.format("BEFORE: target=%s,  source=%s, totalLen=%f", targetVector, sourceVector, totalLen));
		targetVector.setX(targetVector.getFX() + targetAdd.getFX());
		targetVector.setY(targetVector.getFY() + targetAdd.getFY());
		sourceVector.setX(sourceVector.getFX() + sourceAdd.getFX());
		sourceVector.setY(sourceVector.getFY() + sourceAdd.getFY());
		totalLen = VectorHelper.vectorLen(targetVector)*target.getWeight() + VectorHelper.vectorLen(sourceVector)*source.getWeight();
		System.out.println(String.format("AFTER : target=%s,  source=%s, totalLen=%f", targetVector, sourceVector, totalLen));

	}
	
	public Packet produceBall(int startX, int startY, float vX, float vY){
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
		Packet res = new Packet(brick);
		Fpoint vector = res.getVector();
		vector.setX(vX);
		vector.setY(vY);
		Ipoint p = res.getPoint();
		p.setX(startX);
		p.setY(startY);
		return res;
	}
*/	
	/*
	public Walker produceSmallBall(int startX, int startY, float vX, float vY){
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
		Subject s = new Subject(brick);
		Walker res = new Walker(s);
		Fpoint vector = res.getVector();
		vector.setX(vX);
		vector.setY(vY);
		res.getSubject().translate(startX, startY);
		return res;
	}*/
}
