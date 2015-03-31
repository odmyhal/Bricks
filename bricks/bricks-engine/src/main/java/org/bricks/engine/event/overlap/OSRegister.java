package org.bricks.engine.event.overlap;

import java.util.HashMap;
import java.util.Map;

import org.bricks.core.entity.Tuple;
import org.bricks.engine.staff.Liver;

public class OSRegister {

	private static final Map<Class<? extends Liver>, Map<String, OverlapStrategy>> strategies
		= new HashMap<Class<? extends Liver>, Map<String, OverlapStrategy>>();
	
	private static final Map<Class<? extends Liver>, Map<String, OverlapStrategy>> fullStrategies
		= new HashMap<Class<? extends Liver>, Map<String, OverlapStrategy>>();
	
	public static void registerStrategy(Class<? extends Liver<?>> target, String sourceType, OverlapStrategy strategy){
		Map<String, OverlapStrategy> targetStrategies = strategies.get(target);
		if(targetStrategies == null){
			targetStrategies = new HashMap<String, OverlapStrategy>();
			strategies.put(target, targetStrategies);
		}
		targetStrategies.put(sourceType, strategy);
	}
	
	public static final Map<String, OverlapStrategy> getClassStrategies(Class<? extends Liver> liver){
		Map<String, OverlapStrategy> liverStrategies = fullStrategies.get(liver);
		if(liverStrategies == null){
			liverStrategies = new HashMap<String, OverlapStrategy>();
			packClassStrategies(liver, liverStrategies);
			fullStrategies.put(liver, liverStrategies);
		}
		return liverStrategies;
	}
	
	private static final Map<String, OverlapStrategy> packClassStrategies(Class liver, Map<String, OverlapStrategy> liverStrategies){
		Class father = liver.getSuperclass();
		if(!father.equals(Object.class)){
			packClassStrategies(father, liverStrategies);
		}
		Map<String, OverlapStrategy> myStrategies = strategies.get(liver);
		if(myStrategies != null){
			liverStrategies.putAll(myStrategies);
		}
		return liverStrategies;
	}
}
