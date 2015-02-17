package org.bricks.engine.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataPool<D> implements Iterable<D> {
	
	private DataSlot<D>[] data;
	private final ThreadLocal<DataIterator> localIterator = new ThreadLocal<DataIterator>(){
		@Override protected DataIterator initialValue() {
            return new DataIterator();
        }
	};
	
	public DataPool(int size){
		data = new DataSlot[size];
		for(int i = 0; i < size; i++){
			data[i] = new DataSlot();
		}
	}
	
	public boolean removeItem(int i){
		return data[i].removeItem();
	}
	
	public int addItem(D item){
		for(int i=0; i<data.length; i++){
			if(data[i].setItem(item)){
				return i;
			}
		}
		throw new RuntimeException("Pool is full, need handler");
	}

	public Iterator<D> iterator() {
		DataIterator iterator = localIterator.get();
		iterator.reject();
		return iterator;
	}
	
	public Collection<D> collection(){
		Set<D> set = new HashSet<D>();
		Iterator<D> iterator = iterator();
		while(iterator.hasNext()){
			set.add(iterator.next());
		}
		set.remove(null);
		return set;
	}

	private class DataSlot<D>{
		
		private volatile D object;
		private final AtomicBoolean exists = new AtomicBoolean(false);
		
		private boolean setItem(D item){
			if(exists.compareAndSet(true, false)){
				object = item;
				return true;
			}
			return false;
		}
		
		private boolean removeItem(){
			if(exists.compareAndSet(false, true)){
				object = null;
				return true;
			}
			return false;
		}
		
		private D get(){
			return object;
		}
	}
	
	private class DataIterator implements Iterator<D>{
		
		private int cursor = 0;

		public boolean hasNext() {
			return cursor < data.length;
		}

		public D next() {
			D item =  data[cursor++].get();
			if(item == null){
				return null;
			}
			return item;
		}

		public void remove() {
			data[cursor].removeItem();
		}
		
		public void reject(){
			cursor = 0;
		}
		
	}
}
