package org.bricks.engine.event.control;

import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.event.handler.EventHandler;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.item.MultiWalker;

public class AccelerationChangeEvent extends BaseEvent{
	
	public static final int ACCELERATION_EV_GROUP = 3;
	public static final String ACCELERATION_CHANGE_EV_SOURCE = "AccelerationChangeSource@engine.bricks.org";
	
	private float acceleration;
	private String sourceType;
	
	public AccelerationChangeEvent(){
		this(0f);
	}
	
	public AccelerationChangeEvent(float acceleration){
		this(acceleration, ACCELERATION_CHANGE_EV_SOURCE);
	}
	
	public AccelerationChangeEvent(float acceleration, String sourceType){
		this.acceleration = acceleration;
		this.sourceType = sourceType;
	}
/*	
	static{
		EventHandler<MultiWalker, AccelerationChangeEvent> handler = new EventHandler<MultiWalker, AccelerationChangeEvent>(){
			public void processEvent(MultiWalker target, AccelerationChangeEvent event) {
				target.setAcceleration(event.getAcceleration());
			}
		};
		EventHandlerManager.registerHandler(MultiWalker.class, AccelerationChangeEvent.class, ACCELERATION_CHANGE_EV_SOURCE, handler);
	}
*/
	public float getAcceleration(){
		return acceleration;
	}
	
	public void setAcceleration(float acceleration){
		this.acceleration = acceleration;
	}
	
	public int getEventGroupCode() {
		return ACCELERATION_EV_GROUP;
	}
	

	public String sourceType() {
		return sourceType;
	}

	@Override
	public EventSource getEventSource() {
		// TODO Auto-generated method stub
		return null;
	}

}
