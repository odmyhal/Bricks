package org.bricks.extent.space;

import org.bricks.engine.tool.Accelerator;
import org.bricks.engine.tool.Origin;

import com.badlogic.gdx.math.Vector3;

public class Accelerator3D extends Accelerator<Vector3>{
	
	public Accelerator3D(){
		super(3);
	}

	@Override
	protected void transform(Origin<Vector3> src, Origin<Vector3> dst, long curTime) {
		dst.source.x = (float) (this.transform(0, src.source.x, curTime));
		dst.source.y = (float) (this.transform(1, src.source.y, curTime));
		dst.source.z = (float) (this.transform(2, src.source.z, curTime));
	}

	@Override
	protected Origin<Vector3> initOrigin() {
		return new Origin3D();
	}

}
