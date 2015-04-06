package org.bricks.extent.space.overlap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MarkPointPrint<P extends MarkPoint> extends BasePrint<P>{
	
	protected Vector3[] points;

	public MarkPointPrint(PrintStore<P, ?> ps) {
		super(ps);
		points = new Vector3[this.getTarget().size];
		for(int i = 0; i < points.length; i++){
			points[i] = new Vector3();
		}
	}

//	@Override
	public void init() {
		Vector3[] marks = this.getTarget().modifiedMarks;
		for(int i = 0; i < marks.length; i++){
			points[i].set(marks[i]);
		}
	}

	
}
