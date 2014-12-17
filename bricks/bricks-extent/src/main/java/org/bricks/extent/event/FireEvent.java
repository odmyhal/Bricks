package org.bricks.extent.event;

import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.EventSource;


public class FireEvent extends BaseEvent{

	public int getEventGroupCode() {
		return ExtentEventGroups.FIRE_EV_GROUP;
	}

	public String sourceType() {
		return ExtentEventGroups.USER_SOURCE_TYPE;
	}

	@Override
	public EventSource getEventSource() {
		return null;
	}

}
