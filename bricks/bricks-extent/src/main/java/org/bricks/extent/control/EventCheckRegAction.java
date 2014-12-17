package org.bricks.extent.control;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.staff.Liver;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Adding specified Event(s) and EventChecker(s) to target Entity
 * @author oleh
 *
 * @param <L> 
 * @param <W>
 */
public abstract class EventCheckRegAction<L extends Liver, W extends Widget> extends FlowMutableAction<L, W>{
	
	public EventCheckRegAction(L target) {
		super(target);
	}

	private List<Event> events = new LinkedList<Event>();
	private List<EventChecker> checkers = new LinkedList<EventChecker>();

	@Override
	public boolean act(float delta) {
		for(Event e : events){
			target.addEvent(e);
		}
		for(EventChecker checker : checkers){
			target.registerEventChecker(checker);
		}
		clear();
		return true;
	}

	protected void addEvent(Event e){
		events.add(e);
	}
	
	protected void addChecker(EventChecker check){
		checkers.add(check);
	}
	
	public boolean isEmpty(){
		return events.isEmpty() && checkers.isEmpty();
	}
	
	private void clear(){
		events.clear();
		checkers.clear();
	}
}
