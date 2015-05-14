package org.bricks.extent.space;

import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Walk;

import com.badlogic.gdx.math.Vector3;

public abstract class SpaceWalker<S extends SpaceSubject<?, ?, Vector3, Roll3D, ?>, P extends WalkPrint<?, Vector3>> extends MultiWalker<S, P, Vector3, Roll3D>{

	@Override
	protected Walk<Vector3> provideInitialLegs() {
		return new Walk3D(this);
	}

	@Override
	public Origin<Vector3> provideInitialOrigin() {
		return new Origin3D();
	}

	@Override
	protected Roll3D initializeRoll() {
		return new Roll3D();
	}

}
