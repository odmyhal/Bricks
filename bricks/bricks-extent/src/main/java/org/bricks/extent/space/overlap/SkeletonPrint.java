package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.Collection;

import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.tool.Logger;
import org.bricks.exception.Validate;

import com.badlogic.gdx.math.Vector3;

public class SkeletonPrint<P extends Skeleton> extends MarkPointPrint<P>{
	
	public final Triangle[] triangles;
	public final Dimentions3D dimentions = new Dimentions3D();

	public SkeletonPrint(PrintStore<P, ?> ps, int[] indexes) {
		super(ps);
		Validate.isTrue(indexes.length % 3 == 0, "Vrong number of indexes");
		triangles = new Triangle[indexes.length / 3];
		for(int i = 0; i < indexes.length; i += 3){
			triangles[i / 3] = new Triangle(points[indexes[i]], points[indexes[i+1]], points[indexes[i+2]]);
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
