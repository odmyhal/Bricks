package org.bricks.extent.control;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/** Use this action if you want to turn on some effect on Liver with Event,
 *  and then on some condition to turn it off with other Event.
 * @author Oleh Myhal */
public abstract class DoubleEventAction<T extends Liver, W extends Widget> extends FlowMutableAction<T, W>{

	public DoubleEventAction(T target) {
		super(target);
	}
	
	public abstract Event startEvent(W widget);
	
	public abstract boolean finished(float delta);
	
	public abstract Event stopEvent();

	@Override
	public void init(W widget) {
		Event startEvent = startEvent(widget);
		target.addEvent(startEvent);
	}

	@Override
	public boolean act(float delta) {
		boolean result = finished(delta);
		if(result){
			Event stopEvent = stopEvent();
			target.addEvent(stopEvent);
		}
		return result;
	}

}
