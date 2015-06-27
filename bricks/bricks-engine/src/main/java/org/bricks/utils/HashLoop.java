package org.bricks.utils;

import java.util.HashMap;

public class HashLoop<T> extends LinkLoop<T>{
	
	private HashMap<T, Link> index = new HashMap<T, Link>();

	protected LoopIterator defaultIterator(){
		return new HashIterator();
	}
	
	private class HashIterator extends LinkLoop<T>.LoopIterator{

		public void remove() {
			throw new RuntimeException("HashIterator does not support remove");
		}
		
	}

	public boolean add(T val) {
		Link link = findLinkOfVal(val);
		if(link == null){
			link = linkInstance();
			link.setValue(val);
			link.attach();
			index.put(val, link);
			return true;
		}
		return false;
		
	}

	public boolean remove(T val) {
		Link link = findLinkOfVal(val);
		if(link == null){
			return false;
		}
		link.utilize();
		index.remove(val);
		return true;
	}

	public void clear() {
		super.clear();
		index.clear();
	}

	public boolean contains(T val) {
		return findLinkOfVal(val) != null;
	}
	
	protected Link findLinkOfVal(T val){
		return index.get(val);
	}
	
	public static class MultiThreadIterable<K> extends HashLoop<K>{
		
		protected IteratorProvider defaultIteratorProvider(){
			return threadLocalIteratorProvider();
		}
	}
}
