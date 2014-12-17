package org.bricks.engine.event.overlap;

import java.util.Map;

import org.bricks.engine.event.EventTarget;
import org.bricks.engine.pool.Subject;

public interface OverlapTarget extends EventTarget {
	
	public boolean needCheckOverlap(Subject s);
	public Map<String, OverlapStrategy> initOverlapStrategy();
}
