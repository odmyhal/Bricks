package org.bricks.extent.entity;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;
import org.bricks.engine.view.WalkView;
import org.bricks.exception.Validate;

public class DefaultSpeedProvider implements SpeedProvider{
	
	private Walker target;
	
	public DefaultSpeedProvider(Walker walker){
		this.target = walker;
	}

	public Fpoint provideSpeed() {
		WalkView<?> walkView = (WalkView<?>) target.getCurrentView();
		Fpoint vector = walkView.getVector();
		Fpoint res = new Fpoint(vector.getFX(), vector.getFY());
		walkView.free();
		return res;
	}

	public float provideDirectionalSpeed() {
		WalkView<?> walkView = (WalkView<?>) target.getCurrentView();
		Fpoint vector = walkView.getVector();
		float rotation = walkView.getRotation();
		
		float vx = vector.getFX();
		float vy = vector.getFY();
		float curSpeed = (float) Math.sqrt(vx * vx + vy * vy);
		if(curSpeed != 0){
			double mark1 = vx * Math.cos((double) rotation);
			if(mark1 < 0){
				curSpeed *= -1;
			}else if(mark1 == 0){
				double mark2 = vy * Math.sin((double) rotation);
				if(mark2 < 0){
					curSpeed *= -1;
				}
				Validate.isFalse(mark2 == 0, "Considered unreal combination");
			}
		}
		walkView.free();
		return curSpeed;
	}

	
}
