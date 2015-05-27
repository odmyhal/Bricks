package org.bricks.engine.processor.tool;

import org.bricks.engine.staff.Liver;

public interface Approver<T extends Liver> {

	public boolean approve(T target, long curTime);
}
