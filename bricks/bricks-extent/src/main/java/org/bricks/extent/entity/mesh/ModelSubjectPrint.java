package org.bricks.extent.entity.mesh;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.extent.subject.model.MBPrint;

public class ModelSubjectPrint<P extends ModelSubject, EP extends EntityPrint, M extends MBPrint> extends EntityPointsPrint<P, EP>{

	private M modelBrickPrint;
	public ModelSubjectPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	@Override
	protected void init(){
		super.init();
		this.modelBrickPrint = (M) this.getTarget().modelBrick.getSafePrint();
	}
	
	protected void endUse(){
		modelBrickPrint.free();
		modelBrickPrint = null;
		super.endUse();
	}
}
