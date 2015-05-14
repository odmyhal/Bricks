package org.bricks.extent.tool;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;

public class SkeletonDataStore {

	private static final Map<String, Vector3[]> vertexes = new HashMap<String, Vector3[]>();
	private static final Map<String, int[]> indexes = new HashMap<String, int[]>();
	private static final Map<String, int[]> planeIndexes = new HashMap<String, int[]>();
	private static final Map<String, Integer> planeCenterIndexes = new HashMap<String, Integer>();
	
	public static final void registerSkeletonData(String key, Vector3[] vertexData, int[] indexData){
		vertexes.put(key, vertexData);
		indexes.put(key, indexData);
	}
	
	public static final void registerPlaneSkeletonData(String key, Vector3[] vertexData, int[] indexData, 
			int[] planeIndexData, int planeCenterIndex){
		registerSkeletonData(key, vertexData, indexData);
		planeIndexes.put(key, planeIndexData);
		planeCenterIndexes.put(key, planeCenterIndex);
	}
	
	public static final Vector3[] getVertexes(String key){
		return vertexes.get(key);
	}
	
	public static final int[] getIndexes(String key){
		return indexes.get(key);
	}
	
	public static final int[] getPlaneIndexes(String key){
		return planeIndexes.get(key);
	}
	
	public static final int getPlaneCenterIndex(String key){
		return planeCenterIndexes.get(key);
	}
}
