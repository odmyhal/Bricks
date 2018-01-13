package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class FlowTouchListener<W extends Widget> extends InputListener{
	
	protected FlowMutableAction<?, W> flowAction;
	protected W touchPad;
	
	public FlowTouchListener(FlowMutableAction<?, W> action){
		flowAction = action;
	}
	
	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		tuneAction(flowAction);
		return true;
	}
	
	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		tuneAction(flowAction);
	}
	
	protected void tuneAction(FlowMutableAction<?, W> action){
		touchPad.removeAction(action);
		if(action.init(touchPad)){
			touchPad.addAction(action);
		}
	}
	
	public void setWidget(W widget){
		touchPad = widget;
	}
}
