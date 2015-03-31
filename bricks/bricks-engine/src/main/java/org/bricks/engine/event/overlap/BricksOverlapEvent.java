package org.bricks.engine.event.overlap;

import org.bricks.core.entity.Point;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.BrickSubject;

//public class BricksOverlapEvent<T extends Imprint<BrickSubject>, K extends Imprint<BrickSubject>> extends OverlapEvent<T, K, Point>{
public class BricksOverlapEvent<T extends EntityPointsPrint<? extends BrickSubject, ?>, K extends EntityPointsPrint<? extends BrickSubject, ?>> extends OverlapEvent<T, K, Point>{

	public BricksOverlapEvent(T target, K source, Point touchPoint) {
		super(target, source, touchPoint);
		// TODO Auto-generated constructor stub
	}

	public BricksOverlapEvent(T target, K source, Point touchPoint, int crNumb) {
		super(target, source, touchPoint, crNumb);
		// TODO Auto-generated constructor stub
	}

/*	
	private static final AtomicInteger crashNumberGenerator = new AtomicInteger(0);
	private EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> source;
	private EntityPointsPrint<? extends BrickSubject, ? extends EntityPrint> target;
	private Point touchPoint;
	private int crashNum;
	
	private static int generateCrashNumber(){
		return crashNumberGenerator.incrementAndGet();
	}
	
	public BricksOverlapEvent(EntityPointsPrint target, EntityPointsPrint source, Point touchPoint, int crNumb){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = crNumb;
	}
	
	public BricksOverlapEvent(EntityPointsPrint target, EntityPointsPrint source, Point touchPoint){
		this.target = target;
		this.source = source;
		this.touchPoint = touchPoint;
		this.crashNum = generateCrashNumber();
	}
	
	public String sourceType(){
		return source.getTarget().getEntity().sourceType();
	}
	
	public Point getTouchPoint(){
		return touchPoint;
	}
	
	public EntityPointsPrint<? extends Subject, ? extends EntityPrint> getSourcePrint(){
		return source;
	}
	
	public EntityPointsPrint<? extends Subject, ? extends EntityPrint> getTargetPrint(){
		return target;
	}

	public Entity getEventSource() {
		return source.getTarget().getEntity();
	}

	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}
	
	public int getCrashNumber(){
		return crashNum;
	}
	
*/
}
