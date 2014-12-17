package org.bricks.extent.control;

import org.bricks.engine.event.RotationSpeedEvent;
import org.bricks.engine.event.check.RollToMarkChecker;
import org.bricks.engine.staff.Roller;
import org.bricks.engine.view.RollView;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.extent.event.ExtentEventGroups;

import com.badlogic.gdx.math.Vector2;

public class RollEntityAction extends EventCheckRegAction<Roller, FlowTouchPad>{
	
	private static final float rotationCicle = (float) (Math.PI * 2);
	
	private float rotationSpeed;
	
	private Vector2 touchPercentile = new Vector2();

	public RollEntityAction(Roller target, float rotationSpeed) {
		super(target);
		setRotationSpeed(rotationSpeed);
	}
	
	public void setRotationSpeed(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public void init(FlowTouchPad widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		float targetRad = (float) AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		float curSpeed = rotationSpeed;
		float diffRad = targetRad - getEntityRotation();
		if( diffRad > Math.PI || (diffRad < 0 && diffRad > - Math.PI)){
			curSpeed *= -1;
		}
		addEvent(new RotationSpeedEvent(curSpeed));
		addChecker(new RollToMarkChecker(targetRad));
	}

	private float getEntityRotation(){
		RollView rollView = (RollView) target.getCurrentView();
		float rotation = rollView.getRotation();
		rollView.free();
		while(rotation > rotationCicle){
			rotation -= rotationCicle;
		}
		return rotation;
	}
}
