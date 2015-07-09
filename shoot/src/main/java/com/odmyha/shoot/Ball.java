package com.odmyha.shoot;

import org.bricks.exception.Validate;
import org.bricks.annotation.EventHandle;
import org.bricks.annotation.OverlapCheck;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.event.BorderEvent;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.event.check.BorderTouchChecker;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.BrickOverlapAlgorithm;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.event.overlap.SmallEventStrategy;
import org.bricks.engine.help.VectorSwapHelper;
import org.bricks.engine.item.MultiWalkRoller2D;
import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.District;
import org.bricks.engine.staff.ListenDistrictEntity;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.odmyha.subject.BallSubjectNew;
import com.odmyha.weapon.Bullet;
import com.odmyha.weapon.Cannon;
import com.odmyha.weapon.Shield;

public class Ball extends MultiWalkRoller2D<BallSubjectNew, WalkPrint> implements ListenDistrictEntity<WalkPrint>, RenderableProvider{
	
	public static final String BALL_SOURCE_TYPE = "BallSource@shoot.odmyha.com";
/*	
	private static final Map<String, OverlapStrategy> ballStrategy = new HashMap<String, OverlapStrategy>();
	static{
		ballStrategy.put(Ball.BALL_SOURCE_TYPE, OverlapStrategy.TRUE);
		ballStrategy.put(Shield.SHIELD_SOURCE, OverlapStrategy.TRUE);
	}
*/
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
			throw new RuntimeException(String.format("Ball got out of world, vector=%s, origin=%s", getVector(), this.origin().source));
		}
	}
	
	private void reflectOfMoveingPoint(EntityPointsPrint<?, WalkPrint<?, Fpoint>> target, Point touch, Point mVector, long curTime){
		Validate.isTrue(this.equals(target.getTarget().getEntity()));
		Fpoint swap = VectorSwapHelper.fetchReturnVector(target, touch);
//Moveing sake	
		if(mVector.getFX() != 0 || mVector.getFY() != 0){
			Fpoint hitVector = new Fpoint(target.getCenter().getFX() - touch.getFX(), target.getCenter().getFY() - touch.getFY());
			Fpoint addVector = VectorHelper.vectorProjection(mVector, hitVector, new Fpoint());
			if(addVector.getFX() * hitVector.getFX() < 0){
				addVector.setX(0);
			}
			if(addVector.getFY() * hitVector.getFY() < 0){
				addVector.setY(0);
			}
			swap.translate(addVector.getFX(), addVector.getFY());
		}
//end moveing sake		
		Fpoint V = getVector().source;
		V.setX(V.x + swap.x);
		V.setY(V.y + swap.y);
		this.adjustCurrentPrint();
		if(swap.x != 0 || swap.y != 0){
			timerSet(curTime);
//			flushTimer(curTime);
		}
	}

	private void reflectOfPoint(EntityPointsPrint<?, WalkPrint<?, Fpoint>> target, Point touch, long curTime){
		Validate.isTrue(this.equals(target.getTarget().getEntity()));
		Fpoint swap = VectorSwapHelper.fetchReturnVector(target, touch);
		Fpoint V = getVector().source;
		V.setX(V.x + swap.x);
		V.setY(V.y + swap.y);
		this.adjustCurrentPrint();
		if(swap.getFX() != 0 || swap.getFY() != 0){
//			flushTimer(curTime);
			timerSet(curTime);
		}
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Ball.BALL_SOURCE_TYPE, strategyClass = SmallEventStrategy.class)
	@EventHandle(eventType = Ball.BALL_SOURCE_TYPE)
	public void vectorHit(PrintOverlapEvent<EntityPointsPrint<?, WalkPrint<?, Fpoint>>, EntityPointsPrint<?, WalkPrint<?, Fpoint>>, Point> e){
		EntityPointsPrint<?, WalkPrint<?, Fpoint>> target = e.getTargetPrint();
		Validate.isTrue(this.equals(target.entityPrint.getTarget()));
		EntityPointsPrint<?, WalkPrint<?, Fpoint>> source = e.getSourcePrint();
		Fpoint swap = VectorSwapHelper.fetchSwapVector(target, source);
		Fpoint myVector = getVector().source;
		myVector.setX(myVector.x + swap.x);
		myVector.setY(myVector.y + swap.y);
		this.adjustCurrentPrint();
		if(swap.getFX() != 0 || swap.getFY() != 0){
			timerSet(e.getEventTime());
		}
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Shield.SHIELD_SOURCE, strategyClass = OverlapStrategy.TrueOverlapStrategy.class)
	@EventHandle(eventType = Shield.SHIELD_SOURCE)
	public void stoneHit(PrintOverlapEvent<EntityPointsPrint<?, WalkPrint<?, Fpoint>>, EntityPointsPrint<?, WalkPrint<?, Fpoint>>, Point> e){
		reflectOfPoint(e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
	}
	
	@OverlapCheck(algorithm = BrickOverlapAlgorithm.class, sourceType = Cannon.CANNON_SOURCE, strategyClass = OverlapStrategy.TrueOverlapStrategy.class)
	@EventHandle(eventType = Cannon.CANNON_SOURCE)
	public void cannonHit(PrintOverlapEvent<EntityPointsPrint<?, WalkPrint<?, Fpoint>>, EntityPointsPrint<?, WalkPrint>, Point> e){
		EntityPointsPrint<?, WalkPrint> source = e.getSourcePrint();
		RollPrint rv = source.entityPrint;
		if(rv.getRotationSpeed() == 0){
			reflectOfPoint(e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
		}else{
			Point originP = (Point) rv.getOrigin().source;
			Point mVector = VectorSwapHelper.getRollVector(e.getTouchPoint(), originP, rv.getRotationSpeed());
			reflectOfMoveingPoint((EntityPointsPrint<?, WalkPrint<?, Fpoint>>) e.getTargetPrint(), e.getTouchPoint(), mVector, e.getEventTime());
		}
	}
	
	@EventHandle(eventType = Bullet.BULLET_SOURCE)
	public void faceBullet(PrintOverlapEvent e){
		this.outOfWorld(true);
	}
	
	@EventHandle(eventType = Boundary.SIDE_BORDER)
	public void borderSideTouch(BorderEvent e){
		reflectOfPoint((EntityPointsPrint<?, WalkPrint<?, Fpoint>>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
	}
	
	@EventHandle(eventType = Boundary.CORNER_BORDER)
	public void borderCornerTouch(BorderEvent e){
		reflectOfPoint((EntityPointsPrint<?, WalkPrint<?, Fpoint>>) e.getTargetPrint(), e.getTouchPoint(), e.getEventTime());
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

/*	public Map<String, OverlapStrategy> initOverlapStrategy() {
		BrickOverlapAlgorithm algorithm = new BrickOverlapAlgorithm();
		OverlapStrategy trueStrategy = new OverlapStrategy.TrueOverlapStrategy(algorithm);
		Map<String, OverlapStrategy> ballStrategy = new HashMap<String, OverlapStrategy>();
		ballStrategy.put(Ball.BALL_SOURCE_TYPE, new SmallEventStrategy(algorithm));
		ballStrategy.put(Shield.SHIELD_SOURCE, trueStrategy);
		ballStrategy.put(Cannon.CANNON_SOURCE, trueStrategy);
		return ballStrategy;
	}
*/
}
