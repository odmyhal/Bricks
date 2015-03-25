package org.bricks.extent.space;

import org.bricks.engine.tool.Roll;

import com.badlogic.gdx.math.Vector3;

public class Roll3D extends Roll{
	
	private final Vector3 spin = new Vector3(0f, 0f, 99f);
	
	/**
	 * run method in motor thread
	 */
	public void setSpin(Vector3 nsp, long cTime){
		this.setSpin(nsp, 0f, cTime);
	}
	
	public void setRotation(float rotation){
		this.lastRotation = rotation - this.rotation;
		this.rotation = rotation;
	}
	
	public void setSpin(Vector3 nsp, float startRotation, long cTime){
		this.rotation = startRotation;
		flushTimer(cTime);
		spin.set(nsp);
	}
	
	private void setSpeen(float x, float y, float z){
		spin.set(x, y, z);
	}
	
	public Vector3 getSpin(){
		return spin;
	}
}
