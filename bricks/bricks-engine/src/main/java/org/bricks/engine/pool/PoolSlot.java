package org.bricks.engine.pool;

import org.bricks.engine.staff.Entity;


public class PoolSlot<E extends Entity> {
	
	private Subject<E> subject;
	
	public synchronized boolean setSubject(Subject<E> s){
		if(this.subject == null){
			this.subject = s;
			return true;
		}
		return false;
	}
	
	public synchronized Subject<E> freeSubject(){
		if(this.subject == null){
			return null;
		}
		Subject<E> res = this.subject;
		this.subject = null;
		return res;
	}
	
	public synchronized Subject<E> getSubject(){
		return this.subject;
	}

}
