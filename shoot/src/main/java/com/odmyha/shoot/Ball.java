package com.odmyha.shoot;

import java.util.HashMap;
import java.util.Map;

import org.bricks.exception.Validate;
import org.bricks.annotation.EventHandle;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.event.BorderEvent;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.check.BorderTouchChecker;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.event.overlap.SmallEventStrategy;
import org.bricks.engine.help.VectorSwapHelper;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.District;
import org.bricks.engine.staff.ListenDistrictEntity;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.subject.BallSubject;
import com.odmyha.subject.BallSubjectNew;
import com.odmyha.weapon.Bullet;
import com.odmyha.weapon.Cannon;
import com.odmyha.weapon.Shield;

public class Ball extends MultiWalker<BallSubjectNew, WalkPrint> implements ListenDistrictEntity<WalkPrint>, RenderableProvider{
	
	public static final String BALL_SOURCE_TYPE = "BallSource@shoot.odmyha.com";
	
	private static final Map<String, OverlapStrategy> ballStrategy = new HashMap<String, OverlapStrategy>();
	static{
		ballStrategy.put(Ball.BALL_SOURCE_TYPE, OverlapStrategy.TRUE);
		ballStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
	}

	private Ball(BallSubjectNew subj) {
		addSubject(subj);
		registerEventChecker(OverlapChecker.instance());
	}
	
	public static Ball create(Brick brick){
		BallSubjectNew subject = new BallSubjectNew(brick);
		return new Ball(subject);
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(BallSubjectNew subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}
	
	@Override
	public boolean addSubject(BallSubjectNew subj){
		if(getStaff().isEmpty()){
			boolean res = super.addSubject(subj);
			return res;
		}
		return false;
	}
	
	@Override
	public void outOfWorld(){
		outOfWorld(false);
	}
	
	public void outOfWorld(boolean allowed){
		if(allowed){
			super.outOfWorld();
		}else{
			System.out.println(getlog());
			throw new RuntimeException(String.format("Ball got out of world, vector=%s, origin=%s", getVector(), getOrigin()));
		}
	}
	
	private void reflectOfMoveingPoint(SubjectPrint<?, WalkPrint> target, Point touch, Point mVector, long curTime){
		Validate.isTrue(this.equals(target.getTarget().getEntity()));
		Fpoint swap = VectorSwapHelper.fetchReturnVector(target, touch);
//Moveing sake	
		if(mVector.getFX() != 0 || mVector.getFY() != 0){
			Fpoint hitVector = new Fpoint(target.getCenter().getFX() - touch.getFX(), target.getCenter().getFY() - touch.getFY());
			Fpoint addVector = VectorHelper.vectorProjection(mVector, hitVector);
			if(addVector.getFX() * hitVector.getFX() < 0){
				addVector.setX(0);
			}
			if(addVector.getFY() * hitVector.getFY() < 0){
				addVector.setY(0);
			}
			swap.translate(addVector.getFX(), addVector.getFY());
		}
//end moveing sake		
		Fpoint V = getVector();
		V.setX(V.getFX() + swap.getFX());
		V.setY(V.getFY() + swap.getFY());
		this.adjustCurrentPrint();
		if(swap.getFX() != 0 || swap.getFY() != 0){
			flushTimer(curTime);
		}
	}

	private void reflectOfPoint(SubjectPrint<?, WalkPrint> target, Point touch, long curTime){
		Validate.isTrue(this.equals(target.getTarget().getEntity()));
		Fpoint swap = VectorSwapHelper.fetchReturnVector(target, touch);
		Fpoint V = getVector();
		V.setX(V.getFX() + swap.getFX());
		V.setY(V.getFY() + swap.getFY());
		this.adjustCurrentPrint();
		if(swap.getFX() != 0 || swap.getFY() != 0){
			flushTimer(curTime);
		}
	}
	
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void vectorHit(OverlapEvent e){
		SubjectPrint<?, WalkPrint> target = (SubjectPrint<?, WalkPrint>) e.getTargetPrint();
		Validate.isTrue(this.equals(target.entityPrint.getTarget()));
		SubjectPrint<?, WalkPrint> source = (SubjectPrint<?, WalkPrint>) e.getSourcePrint();
		Fpoint swap = VectorSwapHelper.fetchSwapVector(target, source);
		Fpoint myVector = getVector();
		myVector.setX(myVector.getFX() + swap.getFX());
		myVector.setY(myVector.getFY() + swap.getFY());
		this.adjustCurrentPrint();
/*		this.startLog();
		this.appendLog("Hit ball");
		this.appendLog(String.format("My vector=%s,  my center=%s", target.getEntityView().getVector(), target.getEntityView().getOrigin()));
		this.appendLog(String.format("Source vector=%s,  source center=%s", source.getEntityView().getVector(), source.getEntityView().getOrigin()));
		this.appendLog(String.format("Touch point=%s, Result vector=%s", e.getTouchPoint(), myVector));
		this.finishLog();*/
		if(swap.getFX() != 0 || swap.getFY() != 0){
			flushTimer(e.getEventTime());
		}
	}
	
	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void stoneHit(OverlapEvent e){
/*		this.startLog();
		this.appendLog("Hit Stone");
		WalkView wv = (WalkView)e.getTargetView().getEntityView();
		this.appendLog(String.format("My vector=%s,  my center=%s", wv.getVector(), wv.getOrigin()));
		this.appendLog(String.format("Touch Point=%s", e.getTouchPoint()));*/
		reflectOfPoint((SubjectPrint<?, WalkPrint>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
/*		this.appendLog(String.format("Result vector=%s", getVector()));
		this.finishLog();*/
	}
	
	@EventHandle(eventType = Cannon.CANNON_SOURCE)
	public void cannonHit(OverlapEvent e){
		SubjectPrint<?, RollPrint> source = (SubjectPrint<?, RollPrint>) e.getSourcePrint();
		RollPrint rv = source.entityPrint;
/*		this.appendLog("Hit Cannon");
		WalkView wv = (WalkView)e.getTargetView().getEntityView();
		this.appendLog(String.format("My vector=%s,  my center=%s", wv.getVector(), wv.getOrigin()));
		this.appendLog(String.format("Touch Point=%s", e.getTouchPoint()));
		this.appendLog(String.format("Cannon rotationSpeed=%.5f, rotation=%.5f", rv.getRotationSpeed(), rv.getRotation()));
*/		if(rv.getRotationSpeed() == 0){
//			this.appendLog("Simple reflect");
			reflectOfPoint((SubjectPrint<?, WalkPrint>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
		}else{
//			System.out.println(String.format("Reflection of rollable cannon with speed=%.5f", rv.getRotationSpeed()));
			Point mVector = VectorSwapHelper.getRollVector(e.getTouchPoint(), rv.getOrigin(), rv.getRotationSpeed());
			reflectOfMoveingPoint((SubjectPrint<?, WalkPrint>) e.getTargetPrint(), e.getTouchPoint(), mVector, e.getEventTime());
		}
/*		this.appendLog(String.format("Result vector=%s", getVector()));
		this.finishLog();*/
	}
	
	@EventHandle(eventType = Bullet.BULLET_SOURCE)
	public void faceBullet(OverlapEvent e){
		this.outOfWorld(true);
	}
	
	@EventHandle(eventType = Boundary.SIDE_BORDER)
	public void borderSideTouch(BorderEvent e){
/*		this.appendLog("Hit Border 1");
		WalkView wv = (WalkView)e.getTargetView().getEntityView();
		this.appendLog(String.format("My vector=%s,  my center=%s", wv.getVector(), wv.getOrigin()));
		this.appendLog(String.format("Touch Point=%s, border=%s", e.getTouchPoint(), e.getEventSource()));
*/		reflectOfPoint((SubjectPrint<?, WalkPrint>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
/*		this.appendLog(String.format("Result vector=%s", getVector()));
		this.finishLog();*/
	}
	
	@EventHandle(eventType = Boundary.CORNER_BORDER)
	public void borderCornerTouch(BorderEvent e){
/*		this.appendLog("Hit Border 2");
		WalkView wv = (WalkView)e.getTargetView().getEntityView();
		this.appendLog(String.format("My vector=%s,  my center=%s", wv.getVector(), wv.getOrigin()));
		this.appendLog(String.format("Touch Point=%s, border=%s", e.getTouchPoint(), e.getEventSource()));
*/		reflectOfPoint((SubjectPrint<?, WalkPrint>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
/*		this.appendLog(String.format("Result vector=%s", getVector()));
		this.finishLog();*/
	}

	public void onDistrictJoin(District d) {
		if(d.hasBoundaries()){
			registerEventChecker(BorderTouchChecker.instance());
		}else{
			this.unregisterEventChecker(BorderTouchChecker.instance());
		}
	}

	public String sourceType() {
		return BALL_SOURCE_TYPE;
	}

	public Map<String, OverlapStrategy> initOverlapStrategy() {
		Map<String, OverlapStrategy> ballStrategy = new HashMap<String, OverlapStrategy>();
		ballStrategy.put(Ball.BALL_SOURCE_TYPE, new SmallEventStrategy(this));
		ballStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
		ballStrategy.put(Cannon.CANNON_SOURCE, OverlapStrategy.TRUE);
		return ballStrategy;
	}
/*
	public ModelInstance produceModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public ModelInstance getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void disposeModel() {
		// TODO Auto-generated method stub
		
	}
*/
}
