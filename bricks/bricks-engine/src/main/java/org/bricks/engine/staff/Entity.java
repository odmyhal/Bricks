package org.bricks.engine.staff;

import java.util.LinkedList;
import java.util.List;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.Engine;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.view.EntityView;

public interface Entity extends EventSource{
	
	public void applyEngine(Engine engine);
	public Engine getEngine();
	public boolean isEventTarget();
	public float getWeight();
	public List<? extends Subject> getStaff();
	public void translate(int x, int y);

	public void setToRotation(float radians);
	public Ipoint getOrigin();
	
	public LinkedList<EntityView> getViewCache();
	public EntityView getCurrentView();
	public void adjustCurrentView();
	public void outOfWorld();

}
