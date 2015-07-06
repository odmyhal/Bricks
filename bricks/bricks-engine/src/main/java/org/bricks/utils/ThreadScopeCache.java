package org.bricks.utils;

import java.util.LinkedList;

import org.bricks.utils.Cache.DataProvider;

public class ThreadScopeCache<T> extends Cache.LocalCache<T> {

	private static final int maxCacheSize = 256;

	private LinkedList<T> cache = new LinkedList<T>();
	
	public ThreadScopeCache(DataProvider<T> provider){
		super(provider);
	}
	
	public T getLocal(){
		T res = cache.pollFirst();
		if(res == null){
			return provideNew();
		}
		return res;
	}
	
	public void putLocal(T obj){
		if(cache.size() > maxCacheSize){
			return;
		}
		cache.addLast(obj);
	}
	
}
