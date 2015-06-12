package org.bricks.extent.subject.model;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.extent.rewrite.Matrix4Safe;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeDataPrint<P extends NodeData> extends BasePrint<P>{

	protected int lastPrintModified = -3;
	public final Matrix4Safe transform = new Matrix4Safe();
	
	public NodeDataPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

//	@Override
	public void init() {
		P target = getTarget();
		if(lastPrintModified < target.lastPrintModified){
			target.calculateTransform();
			this.transform.set(target.linkTransform());
			lastPrintModified = target.lastPrintModified;
		}
	}
	
}
