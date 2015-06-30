package org.bricks.engine.pool;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.prefs.Preferences;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.data.DataPool;
import org.bricks.utils.Loop;

public class World<R> {
	
	private int sectorLen;
	private int bufferLuft;
	private int sectorInitialCapacity;
//	private int colsCount;
//	private int rowsCount;
	private int minRowNum, maxRowNum, minColNum, maxColNum;
	private final Map<Integer, Map<Integer, District<R, ?>>> sectors = new HashMap<Integer, Map<Integer, District<R, ?>>>();
	
//	private final LinkedList<DataPool<R>> entitiesPool = new LinkedList<DataPool<R>>();
//	private DataPool<R> currentEntities;
	
//	private Collection<R> tmpDecorPool = new HashSet<R>();
//	private volatile Collection<R> decorPool = new HashSet<R>();
	
//	private DataPool<R> decorPool = new DataPool<R>(8);
	
	public World(Preferences props){
		sectorLen = props.getInt("sector.length", 100);
		bufferLuft = props.getInt("buffer.luft", 40);
		sectorInitialCapacity = props.getInt("sector.initial.capacity", 10);
		minRowNum = props.getInt("world.row.min", 0);
		maxRowNum = props.getInt("world.row.max", 10);
		minColNum = props.getInt("world.col.min", 0);
		maxColNum = props.getInt("world.col.max", 10);
//		rowsCount = props.getInt("world.rows.count", 10);
//		colsCount = props.getInt("world.cols.count", 10);
//		System.out.println("Created world with dimentions: " + rowsCount + " : " + colsCount);
		init();
	}
	
	private void init(){
		for(int i = minRowNum; i < maxRowNum + 1; i++){
			for(int j = minColNum; j < maxColNum + 1; j++){
				this.addDistrict(i, j);
			}
		}
		for(int i = minRowNum; i < maxRowNum + 1; i++){
			for(int j : new int[]{minColNum, maxColNum}){
				District bDistrict = getDistrict(i, j);
				if(bDistrict == null){
					System.out.println(String.format("No district: %d : %d", i, j));
				}
				bDistrict.refreshBoundaries();
			}
		}
		for(int j = minColNum + 1; j < maxColNum; j++){
			for(int i : new int[]{minRowNum, maxRowNum}){
				District bDistrict = getDistrict(i, j);
				bDistrict.refreshBoundaries();
			}
		}
	}
	
	
	public synchronized boolean addDistrict(int rowNumber, int colNumber){
		Map<Integer, District<R, ?>> row; 
		if(sectors.containsKey(rowNumber)){
			row = sectors.get(rowNumber);
		}else{
			row = new HashMap<Integer, District<R, ?>>();
			sectors.put(rowNumber, row);
		}
		if(row.containsKey(colNumber)){
			return false;
		}
		Ipoint corner = new Ipoint(colNumber * sectorLen, rowNumber * sectorLen);
		row.put(colNumber, new District(corner, sectorInitialCapacity, sectorLen, bufferLuft, this));
		return true;
	}
	
	public District<R, ?> getDistrict(int rowNumber, int colNumber){
		if(sectors.containsKey(rowNumber)){
			return sectors.get(rowNumber).get(colNumber);
		}
		return null;
	}
	
	public District<R, ?> pointSector(Point point){
		return pointSector(point.getFX(), point.getFY());
	}
	
	public District<R, ?> pointSector(float x, float y){
		int rowNumber = (int) Math.floor(y / sectorLen);
		int colNumber = (int) Math.floor(x / sectorLen);
		return getDistrict(rowNumber, colNumber);
	}
	
	public int defineRowOfPointSectorY(float y){
		return (int) Math.floor(y / sectorLen);
	}
	
	public int defineColOfPointSectorX(float x){
		return (int) Math.floor(x / sectorLen);
	}
	
	public int detectSectorRow(Point point){
		return (int) Math.floor(point.getFY() / sectorLen);
	}
	
	public int detectSectorCol(Point point){
		return (int) Math.floor(point.getFX() / sectorLen);
	}
	
/*	
	public DataPool<R> getRenderEntities(){
		synchronized(entitiesPool){
			currentEntities = entitiesPool.poll();
			if(currentEntities == null){
				currentEntities = new DataPool<R>(entitiesPool);
			}
			currentEntities.occupy();
			for(int i = 0; i < rowsCount; i++){
				for(int j = 0; j < colsCount; j++){
					DataPool<R> districtEntities = getDistrict(i, j).getRenderEntities();
					currentEntities.addAll(districtEntities);
					currentEntities.addAll(decorPool);
					districtEntities.free();
				}
			}
			return currentEntities;
		}
	}
*/
	/**
	 * TODO: make one constant collection instead of creating each time new in getRenderEntities
	 * @return
	 */
/*	public Collection<R> getRenderEntities(){
		Collection<R> result = decorPool.collection();
		for(int i = 0; i < rowsCount; i++){
			for(int j = 0; j < colsCount; j++){
				for(R r : getDistrict(i, j)){
					if(r != null){
						result.add(r);
					}
				}
			}
		}
		return result;
	}
*/	
	
/*	
	public boolean addDecor(R decor){
		decorPool.addItem(decor);
		return true;
	}
	*/
/*	
	public synchronized boolean removeDecor(R decor){
		decorPool.addItem(item)
	}
*/	
/*	public Collection<District<R, ?>> getDistricts(){
		Collection<District<R, ?>> districts = new HashSet<District<R, ?>>();
		for(int i = 0; i < rowsCount; i++){
			for(int j = 0; j < colsCount; j++){
				districts.add(getDistrict(i, j));
			}
		}
		return districts;
	}
*/
}
