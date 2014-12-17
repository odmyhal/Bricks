package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.PanelManager;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.enterprise.d3.help.Vector3Helper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraVerticalRollAction extends FlowMutableAction<Camera, CameraVerticalRollPad>{

	private float rotationSpeed;// = (float) Math.PI;
	private float curRad, tarRad, curSpeed, diffRad;
	
	private Vector3 rotationVector;
	private Vector2 touchPercentile = new Vector2();
	
	public CameraVerticalRollAction(Camera target){
		this(target, PanelManager.cameraDefaults);
	}
	public CameraVerticalRollAction(Camera target, Preferences prefs) {
		super(target);
		rotationVector = target.direction.cpy();
		rotationVector.x = 0;
		rotationVector.y *= -1;
		rotationVector.nor();
		curRad = (float) AlgebraUtils.trigToRadians(rotationVector.y, rotationVector.z);
		rotationSpeed = prefs.getFloat("camera.vertical.roll.speed", PanelManager.cameraDefaults.getFloat("camera.vertical.roll.speed", (float) Math.PI));
	}

	@Override
	public void init(CameraVerticalRollPad widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		tarRad = (float) AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		
		curSpeed = rotationSpeed;
		diffRad = tarRad - curRad;
		if(diffRad < 0){
			curSpeed *= -1;
		}
	}

	@Override
	public boolean act(float delta) {
		boolean stopAction = false;
		diffRad = curSpeed * delta;
		if(Math.abs(diffRad) >= Math.abs(tarRad - curRad)){
			diffRad = tarRad - curRad;
			stopAction = true;
		}
		curRad += diffRad;
		rotationVector = Vector3Helper.instance().calcVectorNormal(target.direction, target.up, rotationVector);
		target.direction.rotateRad(rotationVector, diffRad);
		target.up.rotateRad(rotationVector, diffRad);
		target.update();
		return stopAction;
	}

}
