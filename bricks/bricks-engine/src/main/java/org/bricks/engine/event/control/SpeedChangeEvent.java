package org.bricks.engine.event.control;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.event.handler.EventHandler;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.staff.Walker;

public class SpeedChangeEvent extends BaseEvent {

	public static final int SPEED_EV_GROUP = 4;
	public static final String SPEED_CHANGE_EV_SOURCE = "SpeedChangeSource@engine.bricks.org";
	
	private float newSpeed;
	private String sourceType;
	
	public SpeedChangeEvent(){
		this(0f);
	}
	
	public SpeedChangeEvent(float speed){
		this(speed, SPEED_CHANGE_EV_SOURCE);
	}
	
	public SpeedChangeEvent(float speed, String sourceType){
		this.newSpeed = speed;
		this.sourceType = sourceType;
	}
/*	
	static{
		EventHandler<MultiWalker, SpeedChangeEvent> handler = new EventHandler<MultiWalker, SpeedChangeEvent>(){
			public void processEvent(MultiWalker target, SpeedChangeEvent event) {
				changeWalkerSpeed(target, event.getSpeed());
//				Fpoint vector = target.getVector();
//				float tSpeed = event.getSpeed();
//				double rotation = target.getRotation();
//				vector.setX((float) Math.cos(rotation) * tSpeed);
//				vector.setY((float) Math.sin(rotation) * tSpeed);
//				target.adjustCurrentView();
			}
		};
		EventHandlerManager.registerHandler(MultiWalker.class, SpeedChangeEvent.class, SPEED_CHANGE_EV_SOURCE, handler);
	}
*/
	public float getSpeed(){
		return newSpeed;
	}
	
	public void setSpeed(float speed){
		this.newSpeed = speed;
	}
	
	public int getEventGroupCode() {
		return SPEED_EV_GROUP;
	}
	

	public String sourceType() {
		return sourceType;
	}

	@Override
	public EventSource getEventSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void changeWalkerSpeed(Walker<?, Fpoint> target, float targetSpeed){
		Fpoint vector = target.getVector().source;
		double rotation = target.getRotation();
		vector.setX((float) Math.cos(rotation) * targetSpeed);
		vector.setY((float) Math.sin(rotation) * targetSpeed);
		target.adjustCurrentPrint();
	}
}
