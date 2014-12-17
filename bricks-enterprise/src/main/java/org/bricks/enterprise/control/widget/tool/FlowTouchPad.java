package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class FlowTouchPad extends Touchpad{

	public FlowTouchPad(TouchpadStyle style, FlowTouchListener listener){
		super(1f, style);
		this.setResetOnTouchUp(false);
		this.addFlowTouchListener(listener);
	}
	
	private void addFlowTouchListener(FlowTouchListener listener){
		listener.setWidget(this);
		addListener(listener);
	}
	
}
