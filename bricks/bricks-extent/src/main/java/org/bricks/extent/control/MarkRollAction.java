package org.bricks.extent.control;

import org.bricks.engine.event.Event;
import org.bricks.engine.item.MultiRoller;
import org.bricks.engine.view.RollView;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.engine.event.control.RotationSpeedEvent;

import com.badlogic.gdx.math.Vector2;

/** Use this action if you want to roll entity to any mark.
 * @deprecated such behavior is not efficient need to use Event checker on engines module
 * You have to use org.bricks.extent.control.RollEntityAction instead
 * @author Oleh Myhal */
@Deprecated
public class MarkRollAction<T extends MultiRoller, W extends FlowTouchPad> extends DoubleEventAction<T, W>{
	
	private static final float rotationLag = (float) (Math.PI / 2);
	private static final float rotationCicle = (float) (Math.PI * 2);
	private static final Event stopRollEvent = new RotationSpeedEvent(0f);
	
	private float rotationSpeed;
	
	private float tarRad, curSpeed, diffRad;
	private Vector2 touchPercentile = new Vector2();

	public MarkRollAction(T target, float rSpeed) {
		super(target);
		setRotationSpeed(rSpeed);
	}
	
	public void setRotationSpeed(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public Event startEvent(W widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		tarRad = (float) AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		curSpeed = rotationSpeed;
		diffRad = tarRad - getEntityRotation();
		if( diffRad > Math.PI || (diffRad < 0 && diffRad > - Math.PI)){
			curSpeed *= -1;
		}
		return new RotationSpeedEvent(curSpeed);
	}

	@Override
	public boolean finished(float delta) {
		boolean stopAction = false;
		diffRad = tarRad - getEntityRotation();
		if(curSpeed >= 0){
			if(diffRad <= 0 && diffRad > -Math.PI){
				stopAction = true;
			}
		}
		if(curSpeed < 0){
			if(diffRad >= 0 && diffRad < Math.PI){
				stopAction = true;
			}
		}
		return stopAction;
	}

	@Override
	public Event stopEvent() {
		return stopRollEvent;
	}
	
	private float getEntityRotation(){
		RollView rollView = (RollView) target.getCurrentView();
		float rotation = rollView.getRotation() + rotationLag;
		rollView.free();
		if(rotation > rotationCicle){
			rotation -= rotationCicle;
		}
		return rotation;
	}

}
