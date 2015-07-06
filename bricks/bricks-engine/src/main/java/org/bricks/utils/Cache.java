package org.bricks.utils;

import java.util.HashMap;
import java.util.LinkedList;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Tuple;
import org.bricks.exception.Validate;

public class Cache {
	
	private static final HashMap<Class<?>, ThreadLocal<LocalCache<?>>> cachePool = new HashMap<Class<?>, ThreadLocal<LocalCache<?>>>();
	
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
	
	public static final <K> boolean registerCache(Class<K> cls, final DataProvider<K> provider){
//		System.out.println("CACHE: " + System.currentTimeMillis() + " in thread " + Thread.currentThread().getName() + " registering cache for " + cls + " (" + cls.getCanonicalName() + ")");
		Validate.isFalse(cachePool.containsKey(cls));
		if(cachePool.containsKey(cls)){
			return false;
		}
		cachePool.put(cls, new ThreadLocal<LocalCache<?>>(){
			@Override
			protected LocalCache<K> initialValue(){
				return new ThreadScopeCache<K>(provider);
			}
		});
		return true;
	}
	
	public static final <K extends ThreadTransferCache.TransferData> boolean registerTransferCache(Class<K> clazz, final DataProvider<K> provider){
		Validate.isFalse(cachePool.containsKey(clazz));
		if(cachePool.containsKey(clazz)){
			return false;
		}
		cachePool.put(clazz, new ThreadLocal<LocalCache<?>>(){
			@Override
			protected LocalCache<K> initialValue(){
				return new ThreadTransferCache<K>(provider);
			}
		});
		return true;
	}
	
	public static final <K> K get(Class<K> cls){
		Validate.isTrue(cachePool.containsKey(cls), System.currentTimeMillis() + " in thread " + Thread.currentThread().getName() +  ". There is no registered cache for type " + cls + " (" + cls.getCanonicalName() + ")");
		return (K) cachePool.get(cls).get().getLocal();
	}
	
	public static final <K> void put(K obj){
		((LocalCache<K>) cachePool.get(obj.getClass()).get()).putLocal(obj);
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
