package org.bircks.enterprise.control.panel.camera;

import org.bircks.enterprise.control.panel.Skinner;
import org.bricks.enterprise.control.widget.tool.FlowTouchListener;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.HalfRTouchPad;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;

public class CameraVerticalRollPad extends HalfRTouchPad {
	
	public CameraVerticalRollPad(TouchpadStyle style, FlowTouchListener<CameraVerticalRollPad> listener) {
		super(style, listener);
	}
	
	public static CameraVerticalRollPad produceTouchPad(int radius, Camera camera){
		String realPadName = "cameraVerticalRollBackground-" + radius;
		if(!Skinner.instance().hasFrame(realPadName)){
			Color background = Color.NAVY;
			background.a = 0.5f;
			Color border = Color.OLIVE;
			Skinner.instance().putFrame(radius / 2, radius, 2, background, border, realPadName);
		}
		String knobName = "cameraHorisontalRollKnob-" + radius;
		if(!Skinner.instance().hasFrame(knobName)){
			Color background = Color.RED;
			Skinner.instance().putFrame(13, 13, 0, background, background, knobName);
		}
		TouchpadStyle tps = new TouchpadStyle(Skinner.instance().skin().getDrawable(realPadName), Skinner.instance().skin().getDrawable(knobName));
		
		CameraVerticalRollAction cameraRollAction = new CameraVerticalRollAction(camera);
		FlowTouchListener<CameraVerticalRollPad> listener = new FlowTouchListener<CameraVerticalRollPad>(cameraRollAction);
		CameraVerticalRollPad cameraVerticalRollPad = new CameraVerticalRollPad(tps, listener);
		
//		cameraVerticalRollPad.setSize(cameraVerticalRollPad.getPrefWidth(), cameraVerticalRollPad.getPrefHeight());
		
		
		return cameraVerticalRollPad;
	}

}
