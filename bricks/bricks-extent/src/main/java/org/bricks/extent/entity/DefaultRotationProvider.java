package org.bricks.extent.entity;

import org.bricks.engine.neve.RollPrint;
import org.bricks.engine.staff.Roller;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;

public class DefaultRotationProvider implements RotationProvider{

	private Roller target;
	
	public DefaultRotationProvider(Roller roller){
		this.target = roller;
	}
	
	public float provideRotation() {
		RollPrint<?, ?> rollView = (RollPrint<?, ?>) target.getSafePrint();
		float rotation = rollView.getRotation();
		rollView.free();
		return rotation;
	}

}
