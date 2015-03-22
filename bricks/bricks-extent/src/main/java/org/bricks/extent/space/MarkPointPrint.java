package org.bricks.extent.space;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Vector3;

public class MarkPointPrint<P extends SpaceSubject> extends Imprint<P>{
	
	protected Vector3[] points;

	public MarkPointPrint(PrintStore<P, ?> ps) {
		super(ps);
		points = new Vector3[this.getTarget().markPoint.size];
		for(int i = 0; i < points.length; i++){
			points[i] = new Vector3();
		}
	}

	@Override
	protected void init() {
		Vector3[] marks = this.getTarget().markPoint.modifiedMarks;
		for(int i = 0; i < marks.length; i++){
			points[i].set(marks[i]);
		}
	}

}
