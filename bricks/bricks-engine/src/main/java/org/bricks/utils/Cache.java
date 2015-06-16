package org.bricks.utils;

import java.util.HashMap;
import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Tuple;

public class Cache<T> {
	
	private static final int maxCacheSize = 256;
	private static final HashMap<Class<?>, ThreadLocal<Cache<?>>> cachePool = new HashMap<Class<?>, ThreadLocal<Cache<?>>>();
	
	static{
		registerCache(Fpoint.class, new DataProvider<Fpoint>(){
			public Fpoint provideNew() {
				return new Fpoint();
			}
			
		});
		registerCache(Tuple.class, new DataProvider<Tuple>(){
			public Tuple provideNew() {
				return new Tuple();
			}
			
		});
	}
	
	private LinkedList<T> cache = new LinkedList<T>();
	private DataProvider<T> dataProvider;
	
	private Cache(DataProvider<T> provider){
		this.dataProvider = provider;
	}
	
	private T getLocal(){
		T res = cache.pollFirst();
		if(res == null){
			return dataProvider.provideNew();
		}
		return res;
	}
	
	private void putLocal(T obj){
		if(cache.size() > maxCacheSize){
			return;
		}
		cache.addLast(obj);
	}
	
	public static final <K> void registerCache(Class<K> cls, final DataProvider<K> provider){
		cachePool.put(cls, new ThreadLocal<Cache<?>>(){
			@Override
			protected Cache<K> initialValue(){
				return new Cache<K>(provider);
			}
		});
	}
	
	public static final <K> K get(Class<K> cls){
		return (K) cachePool.get(cls).get().getLocal();
	}
	
	public static final <K> void put(K obj){
		((Cache<K>) cachePool.get(obj.getClass()).get()).putLocal(obj);
	}
	
	public static interface DataProvider<K>{
		public K provideNew();
	}
}
