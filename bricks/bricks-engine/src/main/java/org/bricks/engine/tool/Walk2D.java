package org.bricks.engine.tool;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.staff.Walker;

public class Walk2D implements Walk<Fpoint>{

	private double moveXTime = System.currentTimeMillis();
	private double moveYTime = moveXTime;
	private Walker<?, Fpoint> owner;
//	private int lastMoveX, lastMoveY;
	private Origin2D lastMove = new Origin2D(new Fpoint(0f, 0f));
	private static final int moveLimit = 14;
	
	public Walk2D(Walker<?, Fpoint> walker){
		this.owner = walker;
	}
	
	public void flushTimer(long nTime){
		moveXTime = moveYTime = nTime;
	}
	
	public boolean move(long checkTime, Fpoint p){
		float x = p.x;
		float y = p.y;
		boolean res = false;
		double diffX = checkTime - moveXTime;
		lastMove.source.x = (int) /*Math.round*/(x * diffX / 1000);
		if(lastMove.source.x != 0){
			if(lastMove.source.x > moveLimit){
//				System.out.println("Used move limit: " + lastMoveX);
				lastMove.source.x = moveLimit;
			}else if(lastMove.source.x < -moveLimit){
				lastMove.source.x = -moveLimit;
			}
			res = true;
			moveXTime += /*(int)*/ (lastMove.source.x * 1000 / x);
		}
		double diffY = checkTime - moveYTime;
		lastMove.source.y = (int) /*Math.round*/(y * diffY / 1000);
		if(lastMove.source.y != 0){
			if(lastMove.source.y > moveLimit){
				lastMove.source.y = moveLimit;
			}else if(lastMove.source.y < -moveLimit){
				lastMove.source.y = -moveLimit;
			}
			res = true;
			moveYTime += /*(int)*/ (lastMove.source.y * 1000 / y);
		}
		Validate.isTrue(diffX >= 0 && diffY >= 0);
		if(res){
			owner.translateNoView(lastMove);
		}
		return res;
	}
	
	public boolean moveBack(long checkTime){
		if(lastMove.source.x == 0 && lastMove.source.y == 0){
			return false;
		}
		Validate.isTrue(checkTime >= moveXTime && checkTime >= moveYTime);
		lastMove.set(-lastMove.source.x, -lastMove.source.y);
		owner.translate(lastMove);
		lastMove.set(0f, 0f);
		moveXTime = moveYTime = checkTime;
		return true;
	}
}
