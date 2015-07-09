package org.bricks.engine.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Subject;

public class PrintOverlapEvent<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject<E, ?, ?, ?>>, P, E extends Entity> 
	extends OverlapEvent<T, K, P, E>{

	public PrintOverlapEvent(T target, K source, P touchPoint){
		super(target, source, touchPoint);
	}
	
	public String sourceType(){
		return source.getTarget().getEntity().sourceType();
	}
	
	public K getSourcePrint(){
		return source;
	}
	
	public T getTargetPrint(){
		return target;
	}

	public E getEventSource() {
		return source.getTarget().getEntity();
	}

	public int getEventGroupCode() {
		return BaseEvent.touchEventCode;
	}
	
	public static class PrintOverlapEventExtractor implements OverlapStrategy.EventProducer<Imprint<? extends Subject>, Imprint<? extends Subject<? extends Entity, ?, ?, ?>>, Object>{

		public OverlapEvent<Imprint<? extends Subject>, Imprint<? extends Subject<? extends Entity, ?, ?, ?>>, Object, ? extends EntityCore> produceEvent(
				Imprint<? extends Subject> targetData,
				Imprint<? extends Subject<? extends Entity, ?, ?, ?>> clientData,
				Object touchPoint) {
			return new PrintOverlapEvent(targetData, clientData, touchPoint);
		}

		public OverlapEvent<Imprint<? extends Subject<? extends Entity, ?, ?, ?>>, Imprint<? extends Subject>, Object, ? extends EntityCore> produceRevertEvent(
				Imprint<? extends Subject<? extends Entity, ?, ?, ?>> targetData,
				Imprint<? extends Subject> clientData, Object touchPoint) {
			return new PrintOverlapEvent(targetData, clientData, touchPoint);
		}
		
	}
	

}
