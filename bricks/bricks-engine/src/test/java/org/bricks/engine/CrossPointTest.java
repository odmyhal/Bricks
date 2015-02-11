package org.bricks.engine;

import java.util.ArrayList;
import java.util.List;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.ConvexityApproveHelper;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.view.PointSetView;
import org.junit.Test;

public class CrossPointTest {

	@Test
	public void one(){
		System.out.println(this.getClass().getCanonicalName() + " test one started...");
/*		List<Ipoint> shipPoints = new ArrayList<Ipoint>();
		shipPoints.add(new Ipoint(825 ,  625));
//		shipPoints.add(new Ipoint(824 ,  609));
		shipPoints.add(new Ipoint(824 ,  621));
//		shipPoints.add(new Ipoint(823 ,  597));
		shipPoints.add(new Ipoint(822 ,  610));
//		shipPoints.add(new Ipoint(822 ,  585));
		shipPoints.add(new Ipoint(821 ,  600));
//		shipPoints.add(new Ipoint(820 ,  573));
		shipPoints.add(new Ipoint(820 ,  585));
		shipPoints.add(new Ipoint(819 ,  569));
		shipPoints.add(new Ipoint(818 ,  550));
		shipPoints.add(new Ipoint(818 ,  539));
		shipPoints.add(new Ipoint(819 ,  526));
		shipPoints.add(new Ipoint(820 ,  514));
		shipPoints.add(new Ipoint(821 ,  502));
		shipPoints.add(new Ipoint(824 ,  488));
		shipPoints.add(new Ipoint(826 ,  479));
		shipPoints.add(new Ipoint(831 ,  475));
		shipPoints.add(new Ipoint(834 ,  479));
		shipPoints.add(new Ipoint(835 ,  488));
//		shipPoints.add(new Ipoint(838 ,  502));
		shipPoints.add(new Ipoint(837 ,  508));
		shipPoints.add(new Ipoint(838 ,  519));
		shipPoints.add(new Ipoint(839 ,  530));
		shipPoints.add(new Ipoint(838 ,  539));
		shipPoints.add(new Ipoint(838 ,  550));
		shipPoints.add(new Ipoint(837 ,  561));
		shipPoints.add(new Ipoint(834 ,  573));
		shipPoints.add(new Ipoint(832 ,  585));
		shipPoints.add(new Ipoint(829 ,  597));
		shipPoints.add(new Ipoint(828 ,  609));
//		shipPoints.add(new Ipoint(828 ,  615));
			
		List<Ipoint> stonePoints = new ArrayList<Ipoint>();
		stonePoints.add(new Ipoint(625, 625));
		stonePoints.add(new Ipoint(825, 675));
		stonePoints.add(new Ipoint(975 ,  825));
		stonePoints.add(new Ipoint(975 ,  925));
		stonePoints.add(new Ipoint(925 ,  975));
		stonePoints.add(new Ipoint(875 ,  975));
		stonePoints.add(new Ipoint(725 ,  925));
		stonePoints.add(new Ipoint(625 ,  875));
		stonePoints.add(new Ipoint(575 ,  775));
		
		PointSetView shipView = new PointSetView(shipPoints, new Ipoint(828, 537));
		PointSetView stoneView = new PointSetView(stonePoints, new Ipoint(779, 813));
		
//		ConvexityApproveHelper.applyConvexity(stoneView);
//		ConvexityApproveHelper.applyConvexity(shipView);

		System.out.println("1 sector points: " + shipView.getPointsOfSector(1));
		System.out.println("2 sector points: " + shipView.getPointsOfSector(2));
		System.out.println("3 sector points: " + shipView.getPointsOfSector(3));
		System.out.println("4 sector points: " + shipView.getPointsOfSector(4));
		
		Point touch = OverlapChecker.instance().findOverlapPoint(shipView, stoneView, true);
		System.out.println("Found point - : " + touch);
		
		Point cross = PointHelper.pointCross(new Ipoint(839 ,  502), new Ipoint(838 ,  514), new Ipoint(828, 537), new Ipoint(779, 813));
		System.out.println("Cross point: " + cross);*/
	}
}
