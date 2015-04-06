package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.Collection;


import com.badlogic.gdx.math.Vector3;

public class Skeleton<I extends SkeletonPrint> extends MarkPoint<I>{
	
	public final Collection<Triangle> triangles = new ArrayList<Triangle>();
	private int[] indexes;

	public Skeleton(int[] indexes, Vector3... points){
		super(points);
		this.indexes = indexes;
		for(int i = 0; i < indexes.length; i += 3){
			triangles.add(new Triangle(points[indexes[i]], points[indexes[i+1]], points[indexes[i+2]]));
		}
	}
	
	public I print() {
		return (I) new SkeletonPrint(this.printStore, indexes);
	}
}
