package org.bricks.extent.entity;

import org.bricks.engine.staff.Roller;
import org.bricks.engine.view.RollView;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;

public class DefaultRotationProvider implements RotationProvider{

	private Roller target;
	
	public DefaultRotationProvider(Roller roller){
		this.target = roller;
	}
	
	public float provideRotation() {
		RollView<?> rollView = (RollView<?>) target.getCurrentView();
		float rotation = rollView.getRotation();
		rollView.free();
		return rotation;
	}

}
