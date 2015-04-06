package org.bricks.extent.subject.model;

import org.bricks.engine.neve.BasePrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeDataPrint<P extends NodeData> extends BasePrint<P>{

//	public final Quaternion rotation = new Quaternion();
//	public final Vector3 translation = new Vector3();
//	public final Vector3 scale = new Vector3();
	protected int lastPrintModified = -3;
	public final Matrix4 transform = new Matrix4();
	
	public NodeDataPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

//	@Override
	public void init() {
		P target = getTarget();
		if(lastPrintModified < target.lastPrintModified){
			target.calculateTransform();
			this.transform.set(target.linkTransform());
//			this.rotation.set(target.rotation);
//			this.translation.set(target.translation);
//			this.scale.set(target.scale);
			lastPrintModified = target.lastPrintModified;
		}
	}
	
}
