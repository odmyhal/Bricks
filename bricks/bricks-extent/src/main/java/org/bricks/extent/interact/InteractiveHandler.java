package org.bricks.extent.interact;

import java.util.HashMap;

import org.bricks.engine.staff.Entity;
import org.bricks.extent.subject.model.ModelBrickSubject;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;

import com.badlogic.gdx.math.Vector3;

public class InteractiveHandler {
	
	private static final Loop<Class<? extends Entity>> types = new LinkLoop();
	private static final HashMap<Class<? extends Entity>, Interactive> handlers = new HashMap<Class<? extends Entity>, Interactive>(1);
	
	/**
	 * 
	 * @param clazz
	 * @param interactive
	 * @return boolean indicating if Handler for class already was registered
	 */
	public static final <K extends Entity> boolean registerHandler(Class<K> clazz, Interactive<K> interactive){
		if(handlers.containsKey(clazz)){
			return false;
		}
		types.add(clazz);
		handlers.put(clazz, interactive);
		return true;
	}
	
	public static final <K extends Entity> boolean canHandle(K entity){
		Class tClass = entity.getClass();
		for(Class<? extends Entity> handledClass : types){
			if(handledClass.isAssignableFrom(tClass)){
				return true;
			}
		}
		return false;
	}
	
	public static final <K extends Entity> Interactive<K> getHandle(K entity){
		Class tClass = entity.getClass();
		while(tClass != null){
			for(Class<? extends Entity> handledClass : types){
				if(handledClass.equals(tClass)){
					return handlers.get(handledClass);
				}
			}
			tClass = tClass.getSuperclass();
		}
		return null;
	}
	
	public static interface Interactive<T extends Entity>{
		public void handleTap(T entity, Vector3 touchPoint);
	}

}
