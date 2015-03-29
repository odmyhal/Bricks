package org.bricks.extent.space;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.extent.subject.model.MBPrint;

import com.badlogic.gdx.math.Matrix4;

public class SSPrint<P extends SpaceSubject, EP extends EntityPrint, M extends MBPrint> extends MarkPointPrint<P>{
	
	public M modelBrickPrint;
	public EP entityPrint;
	protected int lastPrintModified = -6;

	public SSPrint(PrintStore<P, ?> ps) {
		super(ps);
	}
	
	protected void init(){
		super.init();
		P target = getTarget();
		entityPrint = (EP) target.getEntity().getSafePrint();
		modelBrickPrint = (M) target.modelBrick.getSafePrint();
	}
	
	protected void endUse(){
		entityPrint.free();
		modelBrickPrint.free();
		entityPrint = null;
		modelBrickPrint = null;
		super.endUse();
	}

}
