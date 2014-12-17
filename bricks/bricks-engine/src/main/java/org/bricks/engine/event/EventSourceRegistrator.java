package org.bricks.engine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bricks.core.entity.Tuple;

/**
 * Not used now
 * @author Oleh Myhal
 *
 */
@Deprecated
public class EventSourceRegistrator {
	
	private static int generator = 0;
	private static final List<Tuple<String, Integer>> sourceIndexes = new ArrayList<Tuple<String, Integer>>();
	private static final Map<String, List<Integer>> classIndexes = new HashMap<String, List<Integer>>(0);

	/**
	 * The only proper way to use this method: static final int MY_SOURCE_TYPE = EventSourceRegistrator.registerEventSource()
	 * event source will be associated with class form which registration executed
	 * @author Oleh Myhal
	 */
	public static synchronized int registerEventSource(){
		int eventSource = generator++;
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		
		List<Integer> indexTypeList;
		if(classIndexes.containsKey(className)){
			indexTypeList = classIndexes.get(className);
		}else{
			indexTypeList = new ArrayList<Integer>();
		}
		int classIndex = indexTypeList.size();
		indexTypeList.add(classIndex, eventSource);
		sourceIndexes.add(eventSource, new Tuple(className, classIndex));
//		System.out.println("\nOLEH-TEST : Registered event source " + className + ", " + classIndex + ", " + eventSource);
		return eventSource; 
	}
	
	public static synchronized int getSourceTypeIndex(int sourceType){
		return sourceIndexes.get(sourceType).getSecond();
	}
	
	public static synchronized String getSourceTypeClass(int sourceType){
		return sourceIndexes.get(sourceType).getFirst();
	}
	
	public static synchronized int getRegisteredSourceType(Class registrator, int index){
		return classIndexes.get(registrator.getCanonicalName()).get(index);
	}
}
