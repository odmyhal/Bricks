package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.Collection;

import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Vector3;

public class SkeletonPrint<P extends Skeleton> extends MarkPointPrint<P>{
	
	public final Collection<Triangle> triangles = new ArrayList<Triangle>();
	public final Dimentions dimentions = new Dimentions();

	public SkeletonPrint(PrintStore<P, ?> ps, int[] indexes) {
		super(ps);
		for(int i = 0; i < indexes.length; i += 3){
			triangles.add(new Triangle(points[indexes[i]], points[indexes[i+1]], points[indexes[i+2]]));
		}
	}
	
	@Override
	public void init() {
		dimentions.reject();
		Vector3[] marks = this.getTarget().modifiedMarks;
		for(int i = 0; i < marks.length; i++){
			points[i].set(marks[i]);
			dimentions.apply(marks[i]);
		}
	}
	
}
