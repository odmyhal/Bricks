package org.bricks.extent.space;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;

import com.badlogic.gdx.math.Matrix4;

public class SSPrint<P extends SpaceSubject, EP extends EntityPrint> extends MarkPointPrint<P>{
	
	protected final Matrix4 transform = new Matrix4();
	public EP entityPrint;
	protected int lastPrintModified = -6;

	public SSPrint(PrintStore<P, ?> ps) {
		super(ps);
	}
	
	protected void init(){
		super.init();
		P target = getTarget();
		entityPrint = (EP) target.getEntity().getSafePrint();
		if(this.lastPrintModified < target.lastPrintModified){
			transform.set(target.linkTransform());
			this.lastPrintModified = target.lastPrintModified;
		}
	}
	
	protected void endUse(){
		entityPrint.free();
		entityPrint = null;
		super.endUse();
	}

}
