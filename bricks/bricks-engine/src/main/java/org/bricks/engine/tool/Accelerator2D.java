package org.bricks.engine.tool;

import org.bricks.core.entity.Fpoint;

public class Accelerator2D extends Accelerator<Fpoint>{
	
	public Accelerator2D(){
		super(2);
	}

	@Override
	protected void transform(Origin<Fpoint> src, Origin<Fpoint> dst, long curTime) {
		dst.source.x = (float) (this.transform(0, src.source.x, curTime));
		dst.source.y = (float) (this.transform(1, src.source.y, curTime));
	}

	@Override
	protected Origin<Fpoint> initOrigin() {
		return new Origin2D();
	}

}
