package org.bricks.extent.space;

import org.bricks.engine.tool.Origin;
import com.badlogic.gdx.math.Vector3;

public class Origin3D extends Origin<Vector3>{

	public Origin3D(Vector3 cntr) {
		super(cntr);
	}
	
	public Origin3D(){
		this(new Vector3());
	}

	@Override
	public void add(Origin<Vector3> trn) {
		source.add(trn.source);
	}

	@Override
	public void set(Origin<Vector3> init) {
		source.set(init.source);
	}

	@Override
	public boolean isZero() {
		return source.x == 0 && source.y == 0 && source.z == 0;
	}

	@Override
	public void mult(float k) {
		source.scl(k);
	}

}
