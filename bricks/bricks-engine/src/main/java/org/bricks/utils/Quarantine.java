package org.bricks.utils;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.exception.Validate;

public class Quarantine<Q> implements Iterable<Q>{
	
	protected AtomicInteger buffCounter = new AtomicInteger(-1);
	protected Slot<Q>[] buffer;
	private int position = 0;
	private Iterator<Q> iterator = new QuarantineIterator();
	
	public Quarantine(int size){
		buffer = new Slot[size];
		for(int i = 0; i < size; i++){
			buffer[i] = new Slot();
		}
	}
	
	public void push(Q object){
		int index = buffCounter.incrementAndGet() % buffer.length;
		Validate.isTrue(buffer[index].data == null, "Buffer is overflow");
		buffer[index].data = object;
	}
	
	public Q poll(){
		if(iterator.hasNext()){
			return iterator.next();
		}
		return null;
	}

	public Iterator<Q> iterator() {
		return iterator;
	}
	
	public boolean isEmpty(){
		return !iterator.hasNext();
	}
	
	private class QuarantineIterator implements Iterator<Q>{

		public boolean hasNext() {
			return buffer[position].data != null;
		}

		public Q next() {
			Q next = buffer[position].data;
			buffer[position].data = null;
			position = ++position % buffer.length;
			return next;
		}

		public void remove() {
			throw new RuntimeException("QuarantineIterator is not allowed to remove items");
		}
		
	}

	private class Slot<Q>{
		private volatile Q data;
	}
	
	public static class MissingQuarantine<K> extends Quarantine<K>{

		public MissingQuarantine(int size) {
			super(size);
		}
		
		public void push(K object){
			int index = buffCounter.incrementAndGet() % buffer.length;
			if(buffer[index].data != null){
				System.out.println("Warning: Quarantine buffer for " + object.getClass().getCanonicalName() + " is overflow");
			}
			buffer[index].data = object;
		}
	}
}
