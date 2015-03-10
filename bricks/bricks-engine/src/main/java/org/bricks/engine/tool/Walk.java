package org.bricks.engine.tool;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;

public class Walk {

	private double moveXTime = System.currentTimeMillis();
	private double moveYTime = moveXTime;
	private Walker owner;
	private int lastMoveX, lastMoveY;
	private static final int moveLimit = 14;
	
	public Walk(Walker walker){
		this.owner = walker;
	}

	
	
	public void flushTimer(long nTime){
		moveXTime = moveYTime = nTime;
	}
/*	
	public boolean move(long checkTime){
		return move(checkTime, vector.getFX(), vector.getFY());
	}
	
	private void updateAcceleration(){
		if(acceleration != 0){
			float rotation = walker.get
		}
	}
*/	
	public boolean move(long checkTime, float x, float y){
		boolean res = false;
		double diffX = checkTime - moveXTime;
		lastMoveX = (int) /*Math.round*/(x * diffX / 1000);
		if(lastMoveX != 0){
			if(lastMoveX > moveLimit){
//				System.out.println("Used move limit: " + lastMoveX);
				lastMoveX = moveLimit;
			}else if(lastMoveX < -moveLimit){
				lastMoveX = -moveLimit;
			}
			res = true;
			moveXTime += /*(int)*/ (lastMoveX * 1000 / x);
		}
		double diffY = checkTime - moveYTime;
		lastMoveY = (int) /*Math.round*/(y * diffY / 1000);
		if(lastMoveY != 0){
			if(lastMoveY > moveLimit){
				lastMoveY = moveLimit;
			}else if(lastMoveY < -moveLimit){
				lastMoveY = -moveLimit;
			}
			res = true;
			moveYTime += /*(int)*/ (lastMoveY * 1000 / y);
		}
		Validate.isTrue(diffX >= 0 && diffY >= 0);
		if(res){
			owner.translateNoView(lastMoveX, lastMoveY);
		}
		return res;
	}
	
	public boolean moveBack(long checkTime){
		if(lastMoveX == 0 && lastMoveY == 0){
			return false;
		}
		Validate.isTrue(checkTime >= moveXTime && checkTime >= moveYTime);
		owner.translate(-lastMoveX, -lastMoveY);
		lastMoveX = lastMoveY = 0;
		moveXTime = moveYTime = checkTime;
		return true;
	}
}
