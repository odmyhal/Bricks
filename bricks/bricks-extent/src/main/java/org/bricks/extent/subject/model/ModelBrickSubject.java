package org.bricks.extent.subject.model;

import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.tool.Roll;

public interface ModelBrickSubject<E extends Entity, I extends Imprint, C, R extends Roll, M extends ModelBrick> extends Subject<E, I, C, R> {

	public M linkModelBrick();
}
