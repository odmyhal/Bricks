package org.bricks.engine.pool;

import java.util.Iterator;
import org.bricks.engine.staff.Subject;
import org.bricks.core.entity.Ipoint;

public class Area extends AreaBase implements Iterable<Subject>{

	public Area(Ipoint corner, int capacity, int len) {
		super(corner, capacity, len);
	}

	public Iterator<Subject> iterator() {
		return new Iterator<Subject>(){
			
			int currentPos = 0;

			public boolean hasNext() {
				return currentPos < pool.length;
			}

			public Subject next() {
				return pool[currentPos++].getSubject();
			}

			public void remove() {
				throw new RuntimeException("Area Iterator has no rights to remove data");
			}
			
		};
	}

}
