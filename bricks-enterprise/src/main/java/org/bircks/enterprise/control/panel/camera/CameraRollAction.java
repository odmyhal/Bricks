package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.PanelManager;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.d3.help.AlgebraUtils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraRollAction extends FlowMutableAction<Camera, FlowTouchPad>{
	
	private double rad, newRad;
	private static final Vector3 rotationVector= new Vector3(0f, 0f, 10f);
	
	private float rotationSpeed;// = Math.PI;
	private double curRad, tarRad, curSpeed, diffRad;
	
	private Vector2 touchPercentile = new Vector2();
	
	public CameraRollAction(Camera camera){
		this(camera, PanelManager.cameraDefaults);
	}
	
	public CameraRollAction(Camera camera, Preferences prefs){
		super(camera);
		curRad = prefs.getFloat("camera.target.constant.rotation", PanelManager.cameraDefaults.getFloat("camera.target.constant.rotation", 0f));
		rotationSpeed = prefs.getFloat("camera.roll.speed.radians", PanelManager.cameraDefaults.getFloat("camera.roll.speed.radians", (float) Math.PI / 2));
	}
	
	@Override
	public boolean act(float delta) {
		boolean result = false;
		rad = curSpeed * delta;
		newRad = curRad + rad;
		if(curSpeed > 0){
			if(newRad > Math.PI * 2){
				newRad -= Math.PI * 2;
				curRad = 0;
			}
			if(newRad >= tarRad && curRad <= tarRad){
				rad -= newRad - tarRad;
				newRad = tarRad;
				result = true;
			}
		}else{
			if(newRad < 0){
				newRad += Math.PI * 2;
				curRad = Math.PI * 2;
			}
			if(newRad <= tarRad && curRad >= tarRad){
				rad -= newRad - tarRad;
				newRad = tarRad;
				result = true;
			}
		}
		target.direction.rotateRad(rotationVector, (float) rad);
		target.up.rotateRad(rotationVector, (float) rad);
		target.update();
		curRad = newRad;
		
		return result;
	}

	@Override
	public void init(FlowTouchPad widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		tarRad = AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		curSpeed = rotationSpeed;
		diffRad = tarRad - curRad;
		if( diffRad > Math.PI || (diffRad < 0 && diffRad > - Math.PI)){
			curSpeed *= -1;
		}
	}
}
