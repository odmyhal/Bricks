package org.bricks.extent.control;

import org.bricks.engine.event.check.RollToMarkProcessorChecker;
import org.bricks.engine.staff.Roller;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.extent.entity.DefaultRotationProvider;

import com.badlogic.gdx.math.Vector2;

public class RollEntityAction extends EventCheckRegAction<Roller, FlowTouchPad>{
	
	protected static final float rotationCycle = (float) (Math.PI * 2);
	
	protected RotationProvider rotationProvider;
	
	private RollToMarkProcessorChecker rollToMarkProcessor;
	
	private float rotationSpeed;
	private Vector2 touchPercentile = new Vector2();
	
	protected float curSpeedRad, curTargetRad;

	public RollEntityAction(Roller target, float rotationSpeed) {
		this(target, new DefaultRotationProvider(target), rotationSpeed);
	}
	
	public RollEntityAction(Roller target, RotationProvider rotationProvider, float rotationSpeed) {
		super(target);
		setRotationSpeed(rotationSpeed);
		this.rotationProvider = rotationProvider;
		rollToMarkProcessor = new RollToMarkProcessorChecker();
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
		float curDiffRad = targetRad - currentRad;
		curTargetRad = targetRad;
		if( curDiffRad > Math.PI || (curDiffRad < 0 && curDiffRad >= -Math.PI)){
			curSpeedRad *= -1;
			if(curTargetRad > currentRad){
				curTargetRad -= rotationCycle;
			}
		}else if(curTargetRad < currentRad){
			curTargetRad += rotationCycle;
		}
		rollToMarkProcessor.init(curTargetRad, curSpeedRad, 0f);
		addChecker(rollToMarkProcessor);
	}

}
