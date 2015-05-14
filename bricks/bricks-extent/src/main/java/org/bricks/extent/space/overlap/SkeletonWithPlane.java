package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.List;

import org.bricks.core.entity.Point;
import org.bricks.extent.space.Point3D;

import com.badlogic.gdx.math.Vector3;

public class SkeletonWithPlane<I extends SkeletonPlanePrint<?>> extends Skeleton<I>{
	
//	private Point3D center;
	private int[] planeIndexes;
	private int centerPlaneIndex;
	protected List<Point> planePoints = new ArrayList<Point>();

	public SkeletonWithPlane(int[] indexes, Vector3[] points, int[] planeIndexes, int centerPlaneIndex) {
		super(indexes, points);
		this.planeIndexes = planeIndexes;
		this.centerPlaneIndex = centerPlaneIndex;
	}

	public I print() {
		return (I) new SkeletonPlanePrint(this.printStore, indexes, planeIndexes, centerPlaneIndex);
	}
}
