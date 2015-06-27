package org.bricks.utils;

import java.util.Iterator;
import java.util.Map;

import org.bricks.core.entity.Tuple;
import org.bricks.utils.Cache.DataProvider;
import org.bricks.utils.LinkLoop.Link;

public class LoopMap<K, V> implements Iterable<V>{
	
	private HashLoop<EntryTuple<K, V>> loop;
	private EntryTuple<K, V> tmpKey = new EntryTuple<K, V>();
	private ValueIterator valueIterator = new ValueIterator();
	
	public LoopMap(){
		this(new HashLoop<EntryTuple<K, V>>());
	}
	
	private LoopMap(HashLoop<EntryTuple<K, V>> tLoop){
		this.loop = tLoop;
	}
	
//Starts map methods	
	public void clear() {
		loop.clear();
	}

	public boolean containsKey(K key) {
		tmpKey.setFirt(key);
		return loop.contains(tmpKey);
	}

	public V get(K key) {
		tmpKey.setFirt(key);
		Link link = loop.findLinkOfVal(tmpKey);
		if(link == null){
			return null;
		}
		EntryTuple<K, V> tuple = (EntryTuple<K, V>) link.getValue();
		return tuple.getSecond();
	}

	public boolean isEmpty() {
		return loop.isEmpty();
	}

	public void put(K key, V val) {
		EntryTuple<K, V> keyTuple = Cache.get(EntryTuple.class);
		keyTuple.setFirt(key);
		keyTuple.setSecond(val);
		loop.add(keyTuple);
	}

	public V remove(K key) {
		tmpKey.setFirt(key);
		Link link = loop.findLinkOfVal(tmpKey);
		if(link == null){
			return null;
		}
		EntryTuple<K, V> tuple = (EntryTuple<K, V>) link.getValue();
		V res = tuple.getSecond();
		tuple.reset();
		Cache.put(tuple);
		return res;
	}
	
	public Loop<? extends Map.Entry<K, V>> entryLoop(){
		return loop;
	}

//Finish map methods
	
	private static class EntryTuple<K1, V1> extends Tuple<K1, V1> implements Map.Entry<K1, V1>{
		
		static{
			Cache.registerCache(EntryTuple.class, new DataProvider<EntryTuple>(){
				public EntryTuple provideNew() {
					return new EntryTuple();
				}
				
			});
		}
		
		public K1 getKey(){
			return first;
		}
		
		public V1 getValue(){
			return second;
		}
		
		public boolean equals(Object object){
			if(this.getClass() != object.getClass()){
				return false;
			}
			EntryTuple t = (EntryTuple) object;
			if(first == null){
				return false;
			}
			return first.equals(t.first);
		}

		public V1 setValue(V1 val) {
			second = val;
			return val;
		}
	}
	
	private class ValueIterator implements Iterator<V>{
		
		private Iterator<EntryTuple<K, V>> loopIterator;

		public boolean hasNext() {
			return loopIterator.hasNext();
		}

		public V next() {
			return loopIterator.next().getSecond();
		}

		public void remove() {
			loopIterator.remove();
		}
		
	}

	public Iterator<V> iterator() {
		this.valueIterator.loopIterator = loop.iterator();
		return valueIterator;
	}
	
	public static class MultiThreadIterable<K2, V2> extends LoopMap<K2, V2>{
		
		public MultiThreadIterable(){
			super(new HashLoop.MultiThreadIterable<EntryTuple<K2, V2>>());
		}
	}
}
