package org.bricks.extent.entity.mesh;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.extent.subject.model.ContainsMBPrint;
import org.bricks.extent.subject.model.MBPrint;

public class ModelSubjectPrint<P extends ModelSubject, EP extends EntityPrint, M extends MBPrint> 
	extends EntityPointsPrint<P, EP>
	implements ContainsMBPrint<P, M>{

	private M modelBrickPrint;
	public ModelSubjectPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

//	@Override
	public void init(){
		super.init();
		this.modelBrickPrint = (M) this.getTarget().modelBrick.getSafePrint();
	}
	
	protected void endUse(){
		modelBrickPrint.free();
		modelBrickPrint = null;
		super.endUse();
	}

	public M linkModelBrickPrint() {
		return modelBrickPrint;
	}
}
