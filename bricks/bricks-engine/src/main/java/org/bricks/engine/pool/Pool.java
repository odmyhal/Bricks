package org.bricks.engine.pool;

import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.engine.staff.Subject;

public interface Pool {
	
	public boolean containsSubject(Subject subject);
	public boolean coverSubject(Subject subject);
	public int capacity();
	public PointSetPrint getPrint();

}
