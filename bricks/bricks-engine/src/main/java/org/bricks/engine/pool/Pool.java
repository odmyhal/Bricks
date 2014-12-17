package org.bricks.engine.pool;

import org.bricks.engine.view.PointSetView;

public interface Pool {
	
	public boolean containsSubject(Subject subject);
	public boolean coverSubject(Subject subject);
	public int capacity();
	public PointSetView getView();

}
