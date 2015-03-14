package org.bricks.extent.entity;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Walker;
import org.bricks.exception.Validate;

public class DefaultSpeedProvider implements SpeedProvider{
	
	private Walker target;
	
	public DefaultSpeedProvider(Walker walker){
		this.target = walker;
	}

	public Fpoint provideSpeed() {
		WalkPrint<?, Fpoint> walkView = (WalkPrint<?, Fpoint>) target.getSafePrint();
		Fpoint vector = walkView.getVector().source;
		Fpoint res = new Fpoint(vector.x, vector.y);
		walkView.free();
		return res;
	}

	public float provideDirectionalSpeed() {
		WalkPrint<?, Fpoint> walkView = (WalkPrint<?, Fpoint>) target.getSafePrint();
		Fpoint vector = walkView.getVector().source;
		float rotation = walkView.getRotation();
		
		float vx = vector.x;
		float vy = vector.y;
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
