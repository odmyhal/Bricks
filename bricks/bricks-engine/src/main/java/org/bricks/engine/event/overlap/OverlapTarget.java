package org.bricks.engine.event.overlap;

import java.util.Map;

import org.bricks.engine.event.EventTarget;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.staff.Subject;

public interface OverlapTarget extends EventTarget {
	
//	public boolean needCheckOverlap(Subject s);
	public OverlapEvent checkOverlap(Subject mySubject, Subject client);
	public Subject checkLastOverlap();
	public Map<String, OverlapStrategy> initOverlapStrategy();
}
