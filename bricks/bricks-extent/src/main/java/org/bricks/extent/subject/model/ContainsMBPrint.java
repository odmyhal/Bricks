package org.bricks.extent.subject.model;

import org.bricks.engine.neve.Imprint;

public interface ContainsMBPrint<P extends ModelBrickSubject, M extends MBPrint> extends Imprint<P>{

	public M linkModelBrickPrint();
}
