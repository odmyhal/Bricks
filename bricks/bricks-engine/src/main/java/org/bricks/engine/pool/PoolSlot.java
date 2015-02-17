package org.bricks.engine.pool;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bricks.engine.staff.Entity;
import org.bricks.exception.Validate;


public class PoolSlot<E extends Entity> {
	
	private volatile Subject<E, ?> subject;
	private final AtomicBoolean exists = new AtomicBoolean(false);
/*	
	public synchronized boolean setSubject(Subject<E, ?> s){
		if(this.subject == null){
			this.subject = s;
			return true;
		}
		return false;
	}
*/
	public boolean setSubject(Subject<E, ?> s){
		if(exists.compareAndSet(false, true)){
			subject = s;
			return true;
		}
		return false;
	}
/*	
	public synchronized Subject<E, ?> freeSubject(){
		if(this.subject == null){
			return null;
		}
		Subject<E, ?> res = this.subject;
		this.subject = null;
		return res;
	}
*/
	public Subject<E, ?> freeSubject(){
		Validate.isTrue(exists.get(), "Slot is empty");
		Subject res = subject;
		subject = null;
		exists.set(false);
		return res;
	}
	
	public Subject<E, ?> getSubject(){
		return this.subject;
	}

}
