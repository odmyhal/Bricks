package org.bricks.engine.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.ConvexityApproveHelper;
import org.bricks.core.help.PointSetHelper;
import org.bricks.core.help.TangPointLocator;

public class PointSetView extends DurableView{
	
	private final Fpoint center = new Fpoint(0f, 0f);
	private List<Ipoint> points;
	private Dimentions dimentions;
	private SectorPoints[] sectorPoints = new SectorPoints[5];

	public PointSetView(LinkedList backet, int size) {
		super(backet);
		for(int i = 1; i < 5; i++){
			sectorPoints[i] = new SectorPoints(i);
		}
		this.points = new ArrayList<Ipoint>(size);
		for(int j=0; j<size; j++){
			this.points.add(new Ipoint(0, 0));
		}
	}
	
	public PointSetView(List<Ipoint> myPoints, Ipoint center){
		//When we do not need durable ability
		super(null);
		for(int i = 1; i < 5; i++){
			sectorPoints[i] = new SectorPoints(i);
		}
		this.points = new ArrayList<Ipoint>(myPoints.size());
		for(int j=0; j<myPoints.size(); j++){
			this.points.add(new Ipoint(0, 0));
		}
		this.init(myPoints, center);
		ConvexityApproveHelper.applyConvexity(this);
	}

	protected void init(List<Ipoint> data, Point center){
		setCenter(center);
		for(int i = 0; i < data.size(); i++){
			Ipoint target = points.get(i);
			Ipoint source = data.get(i);
			target.setX(source.getX());
			target.setY(source.getY());
		}
		for(int k = 1; k < 5; k++){
			sectorPoints[k].rejectSPoints();
		}
		dimentions = null;
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
/*	
	private void setPoints(List<Ipoint> points) {
		this.points = points;
	}
*/	
	public synchronized Dimentions getDimentions() {
		if(dimentions == null){
			dimentions = PointSetHelper.fetchDimentions(points);
		}
		return dimentions;
	}
	
	public Collection<Point> getPointsOfSector(int sNum){
		return sectorPoints[sNum].getSPoints();
	}

	private class SectorPoints{
		
		private Collection<Point> sectorPoints;
		private int sectorNum;
		
		private SectorPoints(int num){
			sectorNum = num;
		}
		
		private synchronized Collection<Point> getSPoints(){
			if(sectorPoints == null){
				sectorPoints = TangPointLocator.findPointsOfSector(points, sectorNum);
			}
			return sectorPoints;
		}
		
		private synchronized void rejectSPoints(){
			sectorPoints = null;
		}
	}
}
