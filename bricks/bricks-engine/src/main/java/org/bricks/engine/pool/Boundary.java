package org.bricks.engine.pool;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.event.EventSource;

public class Boundary implements EventSource{
	
	public static final String CORNER_BORDER = "CorneBorder@engine.bricks.org";
	public static final String SIDE_BORDER = "SideBorder@engine.bricks.org";

	private District district;
	//sector of subjects points which has to be checked for boundary cross (1-4)
	private int fitSide;
	//1 - usual side border, 2 - corner border
	private String boundaryType;
	private Ipoint first, second;
	
	public Boundary(District district, int fSector, String bType, Ipoint one, Ipoint two){
		this.district = district;
		this.fitSide = fSector;
		this.first = one;
		this.second = two;
		this.boundaryType = bType;
	}
	
	public District getDistrict() {
		return district;
	}
	
	public Ipoint getFirst() {
		return first;
	}
	
	public Ipoint getSecond() {
		return second;
	}
	
	public int fitSector(){
		return this.fitSide;
	}

	public String sourceType() {
		return boundaryType;
	}
	
	public String toString(){
		return String.format("Border points: %s - %s, fitSector: %d, District: %d:%d.",  first, second, fitSide, district.rowNumber, district.colNumber);
	}
/*
	@Override
	public boolean isEventTarget() {
		return Boolean.FALSE;
	}
	*/
}
