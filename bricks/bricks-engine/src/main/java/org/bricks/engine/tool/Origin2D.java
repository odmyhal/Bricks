package org.bricks.engine.tool;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;

public class Origin2D extends Origin<Fpoint>{

	public Origin2D(Fpoint cntr) {
		super(cntr);
	}
	
	public Origin2D() {
		super(new Fpoint(0f, 0f));
	}

	@Override
	public void add(Origin<Fpoint> trn) {
		this.source.x += trn.source.x;
		this.source.y += trn.source.y;
	}

	@Override
	public void set(Origin<Fpoint> init) {
		this.source.x = init.source.x;
		this.source.y = init.source.y;
	}
	
	public void set(float x, float y) {
		this.source.x = x;
		this.source.y = y;
	}

	@Override
	public boolean isZero() {
		return source.x == 0 && source.y == 0;
	}

	@Override
	public void mult(float k) {
		this.source.x *= k;
		this.source.y *= k;
	}

}
