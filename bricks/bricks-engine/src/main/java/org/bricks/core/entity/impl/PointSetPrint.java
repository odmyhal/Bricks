package org.bricks.core.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.core.help.PointSetHelper;
import org.bricks.core.help.TangPointLocator;
import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PlanePointsPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.PrintableBase;
import org.bricks.engine.neve.PrintableBrickWrap;

public class PointSetPrint<P extends PrintableBrickWrap> extends BasePrint<P>
	implements PlanePointsPrint<P>{
	
	private final Fpoint center = new Fpoint(0f, 0f);
	private List<Ipoint> points;
	private Dimentions dimentions;
	private PlanePointsPrint.SectorPoints[] sectorPoints = new PlanePointsPrint.SectorPoints[5];

	public PointSetPrint(PrintStore<P, ?> ps) {
		super(ps);
		int size = getTarget().getBrick().size();
		this.points = new ArrayList<Ipoint>(size);
		for(int j=0; j<size; j++){
			this.points.add(new Ipoint(0, 0));
		}
		for(int i = 1; i < 5; i++){
			sectorPoints[i] = new PlanePointsPrint.SectorPoints(i, points);
		}
	}

//	@Override
	public void init() {
		Brick brick = getTarget().getBrick();
		setCenter(brick.getCenter());
		List<Ipoint> data = brick.getPoints();
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

	public List<? extends Point> getPoints() {
		return points;
	}
	
	public Dimentions getDimentions() {
		return dimentions;
	}
	
	public Collection<Point> getPointsOfSector(int sNum){
		return sectorPoints[sNum].getSPoints();
	}
	
	
}
