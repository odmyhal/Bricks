package org.bricks.extent.control;

import org.bricks.engine.event.check.RollToMarkChecker;
import org.bricks.engine.staff.Roller;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;
import org.bricks.enterprise.d3.help.AlgebraUtils;

import com.badlogic.gdx.math.Vector2;

public class RollEntityAction extends EventCheckRegAction<Roller, FlowTouchPad>{
	
	private static final float rotationCycle = (float) (Math.PI * 2);
	private static float halfPI = (float) Math.PI / 2;
	
	protected RotationProvider rotationProvider;
	private float rotationSpeed;
	private Vector2 touchPercentile = new Vector2();
	
	protected float curSpeedRad, curTargetRad;

	public RollEntityAction(Roller target, RotationProvider rotationProvider, float rotationSpeed) {
		super(target);
		setRotationSpeed(rotationSpeed);
		this.rotationProvider = rotationProvider;
	}
	
	public void setRotationSpeed(float rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public void init(FlowTouchPad widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		float targetRad = (float) AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		
		initNewRotation(targetRad, rotationProvider.provideRotation());
	}

	protected void initNewRotation(float targetRad, float currentRad){
		curSpeedRad = rotationSpeed;
		float curDiffRad = targetRad - halfPI;
		while(curDiffRad < 0){
			curDiffRad += rotationCycle;
		}
		while(curDiffRad >= rotationCycle){
			curDiffRad -= rotationCycle;
		}
		curTargetRad = currentRad + curDiffRad;
		if( curDiffRad > Math.PI ){
			curSpeedRad *= -1;
			while(curTargetRad > currentRad){
				curTargetRad -= rotationCycle;
			}
		}else{
			while(curTargetRad < currentRad){
				curTargetRad += rotationCycle;
			}
		}
		addChecker(new RollToMarkChecker(curTargetRad, curSpeedRad));
	}
}
