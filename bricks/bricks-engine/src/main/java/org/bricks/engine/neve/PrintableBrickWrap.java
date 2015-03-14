package org.bricks.engine.neve;

import org.bricks.core.entity.impl.PointSetPrint;
import org.bricks.core.entity.type.Brick;

public interface PrintableBrickWrap<I extends PointSetPrint> extends Printable<I>{

	public Brick getBrick();
}
