package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class HalfRTouchPad extends FlowTouchPad{

	public HalfRTouchPad(TouchpadStyle style, FlowTouchListener listener) {
		super(style, listener);
		validate();
		setSize(getPrefWidth(), getPrefHeight());
	}

	@Override
	public void setSize (float width, float height) {
		super.setSize(width * 2,  height);
		layout();
		super.setSize(width, height);
	}
	
	@Override
	public void invalidate(){
		//do nothing here justprevent super: needsLayout = true;
	}
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		Actor result = super.hit(x, y, touchable);
		result = x < this.getWidth() ? result : null;
		return result;
	}
}
