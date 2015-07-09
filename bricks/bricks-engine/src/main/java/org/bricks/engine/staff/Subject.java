package org.bricks.engine.staff;

import org.bricks.core.entity.Point;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.Printable;
import org.bricks.engine.pool.AreaBase;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Pool;
import org.bricks.engine.pool.World;
import org.bricks.engine.tool.Roll;

public interface Subject<E extends Entity, I extends Imprint, C, R extends Roll> extends Habitant<E>, Printable<I>, Satellite<C, R> {

	public void joinWorld(World world);
	
	public static class SubjectPrintExtractor implements OverlapStrategy.HabitantDataExtractor<Subject, Imprint>{

		public Imprint extractHabitantData(Subject habitant) {
			return habitant.getSafePrint();
		}

		public void freeHabitantData(Imprint clientData) {
			clientData.free();
		}
		
	}
}
