package org.bricks.extent.control;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

public class RiseConstEventButton extends RiseEventButton{
	
	private Event event;

	public RiseConstEventButton(Liver<?> liver, Event evnt, String text) {
		this(liver, evnt, text, provideStyle());
	}
	
	public RiseConstEventButton(Liver<?> liver, Event evnt, String text, TextButtonStyle style) {
		super(liver, text, style);
		this.event = evnt;
	}
	
	protected Event provideEvent(){
		return event;
	}

}
