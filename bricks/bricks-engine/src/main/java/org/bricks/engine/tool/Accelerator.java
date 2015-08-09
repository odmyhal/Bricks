package org.bricks.engine.tool;

import org.bricks.engine.staff.AvareTimer;

public abstract class Accelerator<C> implements AvareTimer{
	
	private Origin<C> tmpAcceleration;
	private FloatAccelerator[] floats;
	
	public Accelerator(int size){
		tmpAcceleration = initOrigin();
		floats = new FloatAccelerator[size];
		for(int i=0; i<floats.length; i++){
			floats[i] = new FloatAccelerator();
		}
	}

	public Origin<C> transformAcceleration(Origin<C> acceleration, long curTime){
		transform(acceleration, tmpAcceleration, curTime);
		return tmpAcceleration;
	}
	
	protected abstract void transform(Origin<C> src, Origin<C> dst, long curTime);
	
	protected abstract Origin<C> initOrigin();
	
	protected int transform(int index, float acc, long curTime){
		return floats[index].transformAcceleration(acc, curTime);
	}
	
	public void timerSet(long time){
		for(FloatAccelerator fa : floats){
			fa.timerSet(time);
		}
	}
	
	public void timerAdd(long time){
		for(FloatAccelerator fa : floats){
			fa.timerAdd(time);
		}
	}
	
	private static class FloatAccelerator implements AvareTimer{
		
		private double checkTime;

		public void timerSet(long time) {
			checkTime = time;
		}

		public void timerAdd(long time) {
			checkTime += time;
		}
		
		private int transformAcceleration(float acceleration, long curTime){
			double diff = curTime - checkTime;
			int valDiff = (int) (acceleration * diff / 1000);
			if(valDiff != 0){
//				System.out.println("Accelerator " + this + ", oldTime: " + checkTime + ", curTime: " + curTime);
//				int calcDiff = (int) ();
				checkTime += valDiff * 1000 / acceleration;
			}
			return valDiff;
		}
		
	}
}
