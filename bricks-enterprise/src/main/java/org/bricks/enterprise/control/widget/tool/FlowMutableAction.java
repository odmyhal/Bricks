package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public abstract class FlowMutableAction<T, W extends Widget> extends Action {

	protected T target;
	
	public FlowMutableAction(T target){
		this.target = target;
	}
	
	public abstract void init(W widget);

}
