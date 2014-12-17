package org.bircks.enterprise.control.panel.camera;

import org.bricks.enterprise.control.widget.tool.FlowMutableAction;

import com.badlogic.gdx.graphics.Camera;

public class CameraAltitudeAction extends FlowMutableAction<Camera, CameraAltitudeSlider>{
	
	private float curAltitude, tarAltitude, curSpeed, diffAltitude;
	private static float altitudeSpeed = 2000f;
	
	public CameraAltitudeAction(Camera camera, float curAltitude){
		super(camera);
//		this.minAltitude = minAltitude;
//		this.maxAltitude = maxAltitude;
		this.curAltitude = curAltitude;
	}

	@Override
	public void init(CameraAltitudeSlider widget) {
		tarAltitude = widget.getValue();
		curSpeed = tarAltitude >= curAltitude ? altitudeSpeed : -altitudeSpeed;
	}

	@Override
	public boolean act(float delta) {
		boolean res = false;
		diffAltitude = curSpeed * delta;
		if(Math.abs(diffAltitude) >= Math.abs(tarAltitude - curAltitude)){
			diffAltitude = tarAltitude - curAltitude;
			res = true;
		}
		curAltitude +=diffAltitude;
		target.translate(0f, 0f, diffAltitude);
		target.update();
		return res;
	}

}
