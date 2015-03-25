package org.bricks.extent.subject.model;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Matrix4;

public class MBPrint<P extends ModelBrick> extends Imprint<P>{

	protected final Matrix4 transformMatrix = new Matrix4();
	protected int lastPrintModified = -6;

	public MBPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	@Override
	protected void init() {
		P target = getTarget();
		if(this.lastPrintModified < target.lastPrintModified){
			transformMatrix.set(target.linkTransform());
			this.lastPrintModified = target.lastPrintModified;
		}
	}

}
