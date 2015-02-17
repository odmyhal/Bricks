package org.bricks.extent.entity.mesh;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeDataPrint<P extends NodeData> extends Imprint<P>{

	public final Quaternion rotation = new Quaternion();
	public final Vector3 translation = new Vector3();
	protected boolean edit;
	
	public NodeDataPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	@Override
	protected void init() {
		edit = getTarget().edit;
		if(edit){
			this.rotation.set(getTarget().rotation);
			this.translation.set(this.getTarget().translation);
		}
	}

}
