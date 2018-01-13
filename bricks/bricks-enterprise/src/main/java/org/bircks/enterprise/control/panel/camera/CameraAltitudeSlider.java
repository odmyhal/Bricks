package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.InteractiveController;
import org.bircks.enterprise.control.panel.Skinner;
import org.bricks.enterprise.control.widget.tool.FlowSlider;
import org.bricks.enterprise.control.widget.tool.FlowTouchListener;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;

public class CameraAltitudeSlider extends FlowSlider{
	
//	private static final float minAltitude = -200f;
//	private static final float maxAltitude = 8000f;//2000f;

	private CameraAltitudeSlider(float minVal, float maxVal, SliderStyle style, FlowTouchListener<CameraAltitudeSlider> listener) {
		super(minVal, maxVal, true, style, listener);
	}
	
	public static CameraAltitudeSlider produceSlider(int height, Camera camera){
		return produceSlider(height, camera, InteractiveController.cameraDefaults);
	}
	
	public static CameraAltitudeSlider produceSlider(int height, Camera camera, Preferences prefs){
		String padName = "cameraAltitudeBackgorund-" + height;
		if(!Skinner.instance().hasFrame(padName)){
			Color background = Color.MAROON;
			background.a = 0.3f;
			Color border = Color.OLIVE;
			Skinner.instance().putFrame(height / 8, height, 2, background, border, padName);
		}
		String knobName = "cameraAltitudeKnob-" + height;
		if(!Skinner.instance().hasFrame(knobName)){
			Color background = Color.CYAN;
			Skinner.instance().putFrame(20, 5, 0, background, background, knobName);
		}
		SliderStyle tps = new SliderStyle(Skinner.instance().skin().getDrawable(padName), Skinner.instance().skin().getDrawable(knobName));
		
		CameraAltitudeAction cameraAltitudeAction = new CameraAltitudeAction(camera, camera.position.z);
		FlowTouchListener<CameraAltitudeSlider> listener = new FlowTouchListener(cameraAltitudeAction);
		
		float minAltitude = prefs.getFloat("camera.altitude.min", InteractiveController.cameraDefaults.getFloat("camera.altitude.min", -500f));
		float maxAltitude = prefs.getFloat("camera.altitude.max", InteractiveController.cameraDefaults.getFloat("camera.altitude.max", 5000f));
		CameraAltitudeSlider slider = new CameraAltitudeSlider(minAltitude, maxAltitude, tps, listener);
		slider.setValue(camera.position.z);
		
		return slider;
	}
}
