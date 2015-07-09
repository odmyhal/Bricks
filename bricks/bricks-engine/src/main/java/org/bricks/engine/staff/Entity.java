package org.bricks.engine.staff;

import java.util.ArrayList;
import java.util.List;

import org.bricks.engine.Engine;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.Printable;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Origin;

public interface Entity<P extends Imprint> extends EntityCore, Printable<P>{
	
	
	public ArrayList<? extends Subject> getStaff();
	

	public void translate(Origin origin);
	public Origin origin();

	public Origin provideInitialOrigin();

//	public void setToRotation(float radians);
	

}
