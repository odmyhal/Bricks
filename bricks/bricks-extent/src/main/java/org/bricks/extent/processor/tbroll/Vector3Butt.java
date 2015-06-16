package org.bricks.extent.processor.tbroll;

import com.badlogic.gdx.math.Vector3;

public class Vector3Butt extends Vector3 implements Butt{
	
	public Vector3Butt(){
		
	}
	
	public Vector3Butt (float x, float y, float z) {
		this.set(x, y, z);
	}

	public void fetchOrigin(Vector3 dest) {
		dest.set(this);
	}

}
