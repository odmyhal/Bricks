package org.bricks.engine.pool;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.exception.Validate;
import org.bricks.engine.staff.Subject;

public class PoolSlot<E extends EntityCore> {
	
	private volatile Habitant<E> subject;
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
	public boolean setSubject(Habitant<E> s){
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
	public Habitant<E> freeSubject(){
		Validate.isTrue(exists.get(), "Slot is empty");
		Habitant res = subject;
		subject = null;
		exists.set(false);
		return res;
	}
	
	public Habitant<E> getSubject(){
		return this.subject;
	}

}
