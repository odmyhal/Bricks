package org.bricks.engine.event.handler;

import java.util.HashMap;
import java.util.Map;

import org.bricks.engine.event.BorderEvent;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.EventTarget;
import org.bricks.engine.event.OverlapEvent;


public class EventHandlerManager {
	
	@SuppressWarnings("rawtypes")
	private static final Map<String, Map<Class, Map<Class, EventHandler>>> handlers = new HashMap<String, Map<Class, Map<Class, EventHandler>>>();

	public static void processEvent(EventTarget target, Event event){
		EventHandler eh = findHandler(target, event);
		eh.processEvent(target, event);
	}
	
	private static EventHandler<?, ?> findHandler(EventTarget target, Event event){
		Map<Class, Map<Class, EventHandler>> eventMap = handlers.get(event.sourceType());
		if(eventMap == null){
			throw new RuntimeException("No handler for sourceType: " + event.sourceType());
		}
		Map<Class, EventHandler> hMap = null;
		Class evClass = event.getClass();
		
		while(evClass != null){
			hMap = eventMap.get(evClass);
			if(hMap != null){
				Class tClass = target.getClass();
				EventHandler rh = null;
				while(tClass != null){
					rh = hMap.get(tClass);
					if(rh != null){
						return rh;
					}
					tClass = tClass.getSuperclass();
				}
			}
			evClass = evClass.getSuperclass();
		}
		throw new RuntimeException("No handler for class" + target.getClass().getCanonicalName()
				+ " event: " + event.getClass().getCanonicalName()
				+ " source: " + event.sourceType());
	}
	
	public static void registerHandler(Class targetClass, Class eventClass, String sourceType, EventHandler handler){
		Map<Class, Map<Class, EventHandler>> eventMap = handlers.get(sourceType);
		if(eventMap == null){
			eventMap = new HashMap<Class, Map<Class, EventHandler>>();
			handlers.put(sourceType, eventMap);
		}
		Map<Class, EventHandler> hMap = eventMap.get(eventClass);
		if(hMap == null){
			hMap = new HashMap<Class, EventHandler>();
			eventMap.put(eventClass, hMap);
		}
		hMap.put(targetClass, handler);
	}

}
