package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class FlowTouchListener<W extends Widget> extends InputListener{
	
	private FlowMutableAction<?, W> flowAction;
	protected W touchPad;
	
	public FlowTouchListener(FlowMutableAction<?, W> action){
		flowAction = action;
	}
	
	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		tuneAction();
		return true;
	}
	
	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		tuneAction();
	}
	
	private void tuneAction(){
		touchPad.removeAction(flowAction);
		flowAction.init(touchPad);
		touchPad.addAction(flowAction);
	}
	
	public void setWidget(W widget){
		touchPad = widget;
	}
}
