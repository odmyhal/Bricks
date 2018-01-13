package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.InputListener;

/*
 * Moves pad knob only on drug event
 */
public class DrugMoveTouchPad extends FlowTouchPad{
	
	private DoubleActionListener listener;

	public DrugMoveTouchPad(TouchpadStyle style, DoubleActionListener listener) {
		super(style, listener);
		this.listener = listener;
		InputListener nativeListener = (InputListener) this.getListeners().get(0);
		listener.setNativeListener(nativeListener);
	}

	public void setKnobPosition(float x, float y){
		listener.setPosition(x, y);
	}
	
	public DoubleActionListener getListener(){
		return listener;
	}
	
	public void layout(){
		super.layout();
		listener.tuneKnobAfterResize(this.getWidth(), this.getHeight());
//		System.out.println("DrugMoveTouchPad layout");
	}
	
	public void rememberKnobPersent(){
		listener.rememberKnobPersent(this.getKnobPercentX(), this.getKnobPercentY());
	}
}
