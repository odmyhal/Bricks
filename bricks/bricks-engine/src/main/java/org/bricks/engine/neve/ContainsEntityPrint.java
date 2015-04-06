package org.bricks.engine.neve;

import org.bricks.engine.staff.Subject;

public interface ContainsEntityPrint<P extends Subject, EP extends EntityPrint> extends Imprint<P>{

	public EP linkEntityPrint();
}
