package org.bricks.engine.staff;

import java.util.Collection;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.event.overlap.OverlapTarget;
import org.bricks.engine.item.Motorable;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.Printable;

public interface Liver<I extends Imprint> extends Entity<I>, OverlapTarget, Logable, Motorable{
}
