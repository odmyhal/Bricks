package org.bricks.utils;

import java.util.HashMap;
import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Tuple;
import org.bricks.exception.Validate;

public class Cache {
	
	public static final String DEFAULT_CACHE_NAME = "default";
	
	private static final HashMap<Class<?>, HashMap<String, ThreadLocal<LocalCache<?>>>> cachePool = new HashMap<Class<?>, HashMap<String, ThreadLocal<LocalCache<?>>>>(16, 1f);
	private static final HashMap<String, ThreadLocal<LocalCache<?>>> HashMap = null;
	
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
		registerCache(LinkLoop.class, new DataProvider<LinkLoop>(){
			public LinkLoop provideNew() {
				return new LinkLoop();
			}
			
		});
	}
	
	public static final <K> boolean registerCache(Class<K> clazz, final DataProvider<K> provider){
		return registerCache(clazz, DEFAULT_CACHE_NAME, provider);
	}
	
	public static final <K> boolean registerCache(Class<K> clazz, String cacheName, final DataProvider<K> provider){
		HashMap<String, ThreadLocal<LocalCache<?>>> classCachePool = cachePool.get(clazz);
		if(classCachePool == null){
			classCachePool = new HashMap<String, ThreadLocal<LocalCache<?>>>(1, 1f);
			cachePool.put(clazz, classCachePool);
		}
		if(classCachePool.containsKey(cacheName)){
			return false;
		}
		classCachePool.put(cacheName, new ThreadLocal<LocalCache<?>>(){
			@Override
			protected LocalCache<K> initialValue(){
				return new ThreadScopeCache<K>(provider);
			}
		});
		return true;
	}
	
	public static final <K extends ThreadTransferCache.TransferData> boolean registerTransferCache(Class<K> clazz, final DataProvider<K> provider){
		return registerTransferCache(clazz, DEFAULT_CACHE_NAME, provider);
	}
	
	public static final <K extends ThreadTransferCache.TransferData> boolean registerTransferCache(Class<K> clazz, String cacheName, final DataProvider<K> provider){
		HashMap<String, ThreadLocal<LocalCache<?>>> classCachePool = cachePool.get(clazz);
		if(classCachePool == null){
			classCachePool = new HashMap<String, ThreadLocal<LocalCache<?>>>(1, 1f);
			cachePool.put(clazz, classCachePool);
		}
		if(classCachePool.containsKey(cacheName)){
			return false;
		}
		classCachePool.put(cacheName, new ThreadLocal<LocalCache<?>>(){
			@Override
			protected LocalCache<K> initialValue(){
				return new ThreadTransferCache<K>(provider);
			}
		});
		return true;
	}
	
	public static final <K> K get(Class<K> cls){
		return get(cls, DEFAULT_CACHE_NAME);
	}
	
	public static final <K> K get(Class<K> cls, String cacheName){
		Validate.isTrue(cachePool.containsKey(cls), System.currentTimeMillis() + " in thread " + Thread.currentThread().getName() +  ". There is no registered cache for type " + cls + " (" + cls.getCanonicalName() + ")");
		return (K) cachePool.get(cls).get(cacheName).get().getLocal();
	}
	
	public static final <K> void put(K obj){
		put(DEFAULT_CACHE_NAME, obj);
	}
	
	public static final <K> void put(String cacheName, K obj){
		((LocalCache<K>) cachePool.get(obj.getClass()).get(cacheName).get()).putLocal(obj);
	}
	
	public static abstract class LocalCache<T>{
		private DataProvider<T> dataProvider;
		
		public LocalCache(DataProvider<T> dataProvider){
			this.dataProvider = dataProvider;
		}
		
		protected T provideNew(){
			return dataProvider.provideNew();
		}
		
		abstract protected T getLocal();
		abstract protected void putLocal(T obj);
	}
	
	public static interface DataProvider<K>{
		public K provideNew();
	}
}
