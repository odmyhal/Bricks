package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;
import org.bricks.engine.neve.PlanePointsPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.extent.space.Point3D;


public class SkeletonPlanePrint<P extends SkeletonWithPlane<?>> extends SkeletonPrint<P>
	implements PlanePointsPrint<P>{

	private final Dimentions planeDimentions = new Dimentions();
	private final List<Point> planePoints = new ArrayList<Point>();
	private Point center;
	private PlanePointsPrint.SectorPoints[] sectorPoints = new PlanePointsPrint.SectorPoints[5];
	
	public SkeletonPlanePrint(PrintStore<P, ?> ps, int[] indexes, int[] planeIndexes, int planeCenterIndex) {
		super(ps, indexes);
		for(int i = 0; i < planeIndexes.length; i++){
			planePoints.add(new Point3D(points[planeIndexes[i]]));
		}
		center = new Point3D(points[planeCenterIndex]);
		for(int i = 1; i < 5; i++){
			sectorPoints[i] = new PlanePointsPrint.SectorPoints(i, planePoints);
		}
	}
	
	@Override
	public void init() {
		super.init();
		planeDimentions.reject();
		for(Point p : planePoints){
			planeDimentions.applyPoint(p);
		}
	}

	public Point getCenter() {
		return center;
	}

	public Dimentions getDimentions() {
		return planeDimentions;
	}

	public Collection<Point> getPointsOfSector(int sNum) {
		return sectorPoints[sNum].getSPoints();
	}

	public List<? extends Point> getPoints() {
		return planePoints;
	}

}
