package org.bricks.extent.space;

import org.bricks.engine.staff.Walker;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Walk;
import org.bricks.exception.Validate;

import com.badlogic.gdx.math.Vector3;

public class Walk3D extends Walk<Vector3>{


	private double moveXTime, moveYTime, moveZTime;
	private boolean rolledBack = false;
/*	
	public Walk3D(Walker<?, Vector3> walker){
		super(walker);
	}
*/	
	protected Origin<Vector3> initLastMoveOrigin(){
		return new Origin3D();
	}
/*	
	public void flushTimer(long nTime){
		moveXTime = moveYTime = moveZTime = nTime;
	}
*/	
	public boolean move(long checkTime, Vector3 p){
		float x = p.x;
		float y = p.y;
		float z = p.z;
		boolean res = false;
		double diffX = checkTime - moveXTime;
		lastMove.source.x = (int) /*Math.round*/(x * diffX / 1000);
		if(lastMove.source.x != 0){
			if(lastMove.source.x > moveLimit){
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
		double diffZ = checkTime - moveZTime;
		lastMove.source.z = (int) /*Math.round*/(z * diffZ / 1000);
		if(lastMove.source.z != 0){
			if(lastMove.source.z > moveLimit){
				lastMove.source.z = moveLimit;
			}else if(lastMove.source.z < -moveLimit){
				lastMove.source.z = -moveLimit;
			}
			res = true;
			moveZTime += /*(int)*/ (lastMove.source.z * 1000 / z);
		}
		Validate.isTrue(diffX >= 0 && diffY >= 0 && diffZ >= 0);
		if(res && rolledBack){
//			owner.translateNoView(lastMove);
			rolledBack = false;
		}
		return res;
	}
	
	@Override
	public boolean moveBack(long checkTime, float k){
		if(rolledBack || lastMove.isZero()){
			return false;
		}
		Validate.isTrue(checkTime >= moveXTime && checkTime >= moveYTime && checkTime >= moveZTime);
		lastMove.mult(-1f * k);
//		owner.translate(lastMove);
//		lastMove.mult(0f);
		rolledBack = true;
		moveXTime = moveYTime = moveZTime = checkTime;
		return true;
	}
	
	public void timerSet(long time) {
		moveXTime = moveYTime = moveZTime = time;
		
	}
	public void timerAdd(long time) {
		moveXTime += time;
		moveYTime += time;
		moveZTime += time;
	}
}
