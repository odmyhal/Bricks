package org.bricks.engine.staff;

import org.bricks.engine.Engine;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.tool.Origin;

public interface EntityCore extends EventSource {

	public void applyEngine(Engine engine);
	public Engine getEngine();
	
	//TODO check if we really need this:
//	public boolean isEventTarget();
	

	public void disappear();
	public void outOfWorld();
	
}
