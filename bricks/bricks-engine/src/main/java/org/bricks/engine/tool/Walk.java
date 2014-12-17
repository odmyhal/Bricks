package org.bricks.engine.tool;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;

public class Walk {

	private Fpoint vector;
	private long moveXTime = System.currentTimeMillis();
	private long moveYTime = moveXTime;
	private Walker owner;
	private int lastMoveX, lastMoveY;
	private final int moveLimit = 14;
	
	public Walk(Walker walker){
		this.owner = walker;
		vector = new Fpoint(0f, 0f);
	}

	public void setVector(Fpoint vector) {
		this.vector.setX(vector.getFX());
		this.vector.setY(vector.getFY());
	}
	
	public void setVector(float x, float y){
		this.vector.setX(x);
		this.vector.setY(y);
	}

	public Fpoint getVector() {
		return vector;
	}
	
	public void flushTimer(long nTime){
		moveXTime = moveYTime = nTime;
	}
	
	public boolean move(long checkTime){
		return move(checkTime, vector.getFX(), vector.getFY());
	}
	
	private boolean move(long checkTime, float x, float y){
		boolean res = false;
		long diffX = checkTime - moveXTime;
		lastMoveX = (int) /*Math.round*/(x * diffX / 1000);
		if(lastMoveX != 0){
			if(lastMoveX > moveLimit){
				lastMoveX = moveLimit;
			}else if(lastMoveX < -moveLimit){
				lastMoveX = -moveLimit;
			}
			res = true;
			moveXTime += (int) (lastMoveX * 1000 / x);
		}
		long diffY = checkTime - moveYTime;
		lastMoveY = (int) /*Math.round*/(y * diffY / 1000);
		if(lastMoveY != 0){
			if(lastMoveY > moveLimit){
				lastMoveY = moveLimit;
			}else if(lastMoveY < -moveLimit){
				lastMoveY = -moveLimit;
			}
			res = true;
			moveYTime += (int) (lastMoveY * 1000 / y);
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
