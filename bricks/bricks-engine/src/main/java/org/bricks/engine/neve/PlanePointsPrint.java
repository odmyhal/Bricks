package org.bricks.engine.neve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;
import org.bricks.core.help.TangPointLocator;
import org.bricks.exception.Validate;

public interface PlanePointsPrint<P extends Printable> extends Imprint<P> {

	public Point getCenter();
	public Dimentions getDimentions();
	public Collection<Point> getPointsOfSector(int sNum);
	public List<? extends Point> getPoints();
	
	public static class SectorPoints{
		
		private volatile Collection<Point> sectorPoints;
		private Collection<? extends Point> allPoints;
		private int sectorNum;
		
		public SectorPoints(int num, Collection<? extends Point> points){
			allPoints = points;
			sectorNum = num;
			sectorPoints = new ArrayList<Point>();
		}
		
		public Collection<Point> getSPoints(){
			Collection<Point> result = sectorPoints;
			if(result.isEmpty()){
				result = TangPointLocator.findPointsOfSector(allPoints, sectorNum, result);
				Validate.isFalse(result.isEmpty(), "Sector points list can not be empty");
				sectorPoints = result;
			}
			return result;
		}
		
		public void rejectSPoints(){
			sectorPoints.clear();
		}
	}
}
