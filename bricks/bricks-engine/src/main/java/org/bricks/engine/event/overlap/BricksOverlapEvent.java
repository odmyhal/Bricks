package org.bricks.engine.event.overlap;

import org.bricks.core.entity.Point;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.staff.Entity;

//public class BricksOverlapEvent<T extends Imprint<BrickSubject>, K extends Imprint<BrickSubject>> extends OverlapEvent<T, K, Point>{
public class BricksOverlapEvent<T extends EntityPointsPrint<? extends BrickSubject, ?>, K extends EntityPointsPrint<? extends BrickSubject<Entity, ?>, ?>> extends PrintOverlapEvent<T, K, Point, Entity>{

	public BricksOverlapEvent(T target, K source, Point touchPoint) {
		super(target, source, touchPoint);
	}
/*
	public BricksOverlapEvent(T target, K source, Point touchPoint, int crNumb) {
		super(target, source, touchPoint, crNumb);
	}
*/

}
