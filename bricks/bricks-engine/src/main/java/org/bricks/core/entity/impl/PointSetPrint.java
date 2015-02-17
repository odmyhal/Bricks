package org.bricks.core.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointSetHelper;
import org.bricks.core.help.TangPointLocator;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

public class PointSetPrint<P extends BrickWrap> extends Imprint<P>{
	
	private final Fpoint center = new Fpoint(0f, 0f);
	private List<Ipoint> points;
	private Dimentions dimentions;
	private SectorPoints[] sectorPoints = new SectorPoints[5];

	public PointSetPrint(PrintStore<P, ?> ps) {
		super(ps);
		for(int i = 1; i < 5; i++){
			sectorPoints[i] = new SectorPoints(i);
		}
		int size = getTarget().brick.size();
		this.points = new ArrayList<Ipoint>(size);
		for(int j=0; j<size; j++){
			this.points.add(new Ipoint(0, 0));
		}
	}

	@Override
	protected void init() {
		setCenter(getTarget().brick.getCenter());
		List<Ipoint> data = getTarget().brick.getPoints();
		for(int i = 0; i < data.size(); i++){
			Ipoint target = points.get(i);
			Ipoint source = data.get(i);
			target.setX(source.getX());
			target.setY(source.getY());
		}
		for(int k = 1; k < 5; k++){
			sectorPoints[k].rejectSPoints();
		}
		dimentions = PointSetHelper.fetchDimentions(points);
	}

	public Point getCenter() {
		return center;
	}

	private void setCenter(Point center) {
		this.center.setX(center.getFX());
		this.center.setY(center.getFY());
	}

	public List<Ipoint> getPoints() {
		return points;
	}
	
	public Dimentions getDimentions() {
		return dimentions;
	}
	
	public Collection<Point> getPointsOfSector(int sNum){
		return sectorPoints[sNum].getSPoints();
	}
	
	private class SectorPoints<P>{
		
		private volatile Collection<Point> sectorPoints;
		private int sectorNum;
		
		private SectorPoints(int num){
			sectorNum = num;
		}
		
		private Collection<Point> getSPoints(){
			Collection<Point> result = sectorPoints;
			if(result == null){
				result = TangPointLocator.findPointsOfSector(points, sectorNum);
				sectorPoints = result;
			}
			return result;
		}
		
		private void rejectSPoints(){
			sectorPoints = null;
		}
	}
}
