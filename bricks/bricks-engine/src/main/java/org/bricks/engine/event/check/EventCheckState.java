package org.bricks.engine.event.check;

public class EventCheckState {
	
	private Object current;
	private Object last;
	private int entityState = 0;
	private int areaState = 0;
	
	public boolean checkNew(Object o){
		if(o.equals(current)){
			return false;
		}
		current = o;
		entityState = 0;
		areaState = 0;
		last = null;
		return true;
	}

	public Object getLast() {
		return last;
	}

	public void setLast(Object last) {
		this.last = last;
	}
	
	public int getAndIncrementAreaState(){
		return areaState++;
	}
	
	public int getEntityState(){
		return entityState;
	}
	
	public int incrementEntityState(){
		areaState = 0;
		return ++entityState;
	}
	
	public void reject(){
		current = null;
		last = null;
		areaState = 0;
		entityState = 0;
	}
}
