package org.bricks.engine.staff;

import java.util.ArrayList;
import java.util.List;

import org.bricks.engine.Engine;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.Printable;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;

public interface Entity<P extends Imprint> extends EventSource, Printable<P>{
	
	public void applyEngine(Engine engine);
	public Engine getEngine();
	public boolean isEventTarget();
	public ArrayList<? extends Subject> getStaff();
	public void translate(Origin origin);

//	public void setToRotation(float radians);
	public Origin origin();
	
	public void disappear();
	public void outOfWorld();
	
	public Origin provideInitialOrigin();

}
