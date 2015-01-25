package org.bricks.engine.event.control;

import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.event.handler.EventHandler;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.staff.Roller;

public class RotationSpeedEvent extends BaseEvent{
	
	public static final int ROTATION_EV_GROUP = 2;
	public static final String ROTATION_EV_SOURCE = "RotationSource@engine.bricks.org";
	
	static{
		EventHandler<MultiRoller, RotationSpeedEvent> handler = new EventHandler<MultiRoller, RotationSpeedEvent>(){
			public void processEvent(MultiRoller target, RotationSpeedEvent event) {
				changeRollerRotationSpeed(target, event.getEventTime(), event.getRotationSpeed());
//				target.setRotationSpeed(event.getRotationSpeed());
//				target.flushTimer(event.getEventTime());
//				target.adjustCurrentView();
			}
		};
		EventHandlerManager.registerHandler(MultiRoller.class, RotationSpeedEvent.class, ROTATION_EV_SOURCE, handler);
	}

	private float rotationSpeed;
	private String sourceType;
	
	public RotationSpeedEvent(float rotationSpeed){
		this(rotationSpeed, ROTATION_EV_SOURCE);
	}
	
	public RotationSpeedEvent(float rotationSpeed, String sourceType){
		this.rotationSpeed = rotationSpeed;
		this.sourceType = sourceType;
	}
	
	public int getEventGroupCode() {
		return ROTATION_EV_GROUP;
	}

	public String sourceType() {
		return sourceType;
	}

	@Override
	public EventSource getEventSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public float getRotationSpeed(){
		return rotationSpeed;
	}

	public static void changeRollerRotationSpeed(Roller target, long changeTime, float newSpeed){
		target.setRotationSpeed(newSpeed);
		target.flushTimer(changeTime);
		target.adjustCurrentView();
	}
}
