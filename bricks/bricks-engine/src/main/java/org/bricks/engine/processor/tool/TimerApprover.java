package org.bricks.engine.processor.tool;

import org.bricks.engine.staff.Liver;

public class TimerApprover<T extends Liver> implements Approver<T> {
	
	private long timePeriod, checkTime;

	public TimerApprover(long timePeriod){
		this.timePeriod = timePeriod;
	}
	
	public boolean approve(T target, long curTime) {
		return curTime - checkTime > timePeriod;
	}
	
	public void setCheckTime(long checkTime){
		this.checkTime = checkTime;
	}

}
