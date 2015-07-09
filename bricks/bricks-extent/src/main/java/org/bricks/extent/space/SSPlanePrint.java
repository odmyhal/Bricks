package org.bricks.extent.space;

import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PlanePointsPrint;
import org.bricks.engine.neve.PrintFactory;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.Printable;
import org.bricks.extent.subject.model.MBPrint;

public class SSPlanePrint<P extends SpaceSubject<?, ?, ?, ?, ?>, EP extends EntityPrint, M extends MBPrint>
	extends SSPrint<P, EP, M>
	implements PlanePointsPrint<P>{
	
	public static final PrintFactory<SSPlanePrint> printFactory = new SSPlanePrintFactory();

	public SSPlanePrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	public Point getCenter() {
		return this.linkModelBrickPrint().getSkeletonPlanePrint().getCenter();
	}

	public Dimentions getDimentions() {
		return this.linkModelBrickPrint().getSkeletonPlanePrint().getDimentions();
	}

	public Collection<Point> getPointsOfSector(int sNum) {
		return this.linkModelBrickPrint().getSkeletonPlanePrint().getPointsOfSector(sNum);
	}

	public List<? extends Point> getPoints() {
		return this.linkModelBrickPrint().getSkeletonPlanePrint().getPoints();
	}

	private static class SSPlanePrintFactory implements PrintFactory<SSPlanePrint>{

		public SSPlanePrint producePrint(PrintStore<? extends Printable, SSPlanePrint> printStore) {
			return new SSPlanePrint(printStore);
		}
		
	}
}
