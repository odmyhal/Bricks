package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.InteractiveController;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraMoveAction extends RotationDependAction<Camera, FlowTouchPad> {
	
	private float curX = 0f, curY = 0f;
	private float tarX, tarY, moveX, moveY;
	private float maxRadius, moveSpeed;
	
	
	
	/**
	 * speed in x and y perspective
	 */
	private Vector2 moveDirection = new Vector2();
	private Vector2 translateTMP = new Vector2();
	

	public CameraMoveAction(Camera target) {
		this(target, InteractiveController.cameraDefaults);
	}
	
	public CameraMoveAction(Camera target, Preferences prefs) {
		super(target, prefs.getFloat("camera.target.constant.rotation", InteractiveController.cameraDefaults.getFloat("camera.target.constant.rotation", 0f)));
		initPreferences(prefs);
	}
	
	public CameraMoveAction(Camera target, RotationDependAction.RotationProvider rotationProvider){
		this(target, rotationProvider, InteractiveController.cameraDefaults);
	}
	
	public CameraMoveAction(Camera target, RotationDependAction.RotationProvider rotationProvider, Preferences prefs) {
		super(target, rotationProvider);
		initPreferences(prefs);
	}
	
	private void initPreferences(Preferences prefs){
		this.maxRadius = prefs.getFloat("camera.move.maxRadius", InteractiveController.cameraDefaults.getFloat("camera.move.maxRadius", 1000f));
		this.moveSpeed = prefs.getFloat("camera.move.speed", InteractiveController.cameraDefaults.getFloat("camera.move.speed", 1000f));
	}
	
	@Override
	public boolean act(float delta) {
		boolean res = false;
		moveX = moveDirection.x * delta;
		moveY = moveDirection.y * delta;
		
		if(Math.abs(moveX) > Math.abs(tarX - curX)
				|| Math.abs(moveY) > Math.abs(tarY - curY)){
			moveX = tarX - curX;
			moveY = tarY - curY;
			res = true;
		}
		curX += moveX;
		curY += moveY;
		
		translateTMP.set(moveX, moveY);
		translateTMP.rotateRad(getRotation());
		
		target.translate(translateTMP.x, translateTMP.y, 0);
		target.update();
		
		return res;
	}

	@Override
	public void init(FlowTouchPad widget) {
		tarX = widget.getKnobPercentX() * maxRadius;
		tarY = widget.getKnobPercentY() * maxRadius;
		moveDirection.set(tarX - curX, tarY - curY);
		moveDirection.scl(moveSpeed / moveDirection.len());
	}
}
