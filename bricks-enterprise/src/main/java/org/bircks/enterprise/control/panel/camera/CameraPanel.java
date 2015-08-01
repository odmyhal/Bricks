package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.AnimationRisePanel;
import org.bircks.enterprise.control.panel.Skinner;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.FlowWidgetProvider;
import org.bricks.enterprise.control.widget.tool.HalfRTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CameraPanel extends AnimationRisePanel{
	
	protected Camera camera;
	protected RotationDependAction.RotationProvider rotationProvider;
	protected Preferences widgetDefaults;
	protected Preferences cameraDefaults;
	
	public CameraPanel(Camera camera){
		this(camera, "panel.defaults", "camera.defaults");
	}
	
	public CameraPanel(Camera camera, RotationDependAction.RotationProvider rotationProvider){
		this(camera, rotationProvider, "panel.defaults", "camera.defaults");
	}
	
	public CameraPanel(Camera camera, String widgetPrefName, String cameraPrefName){
		this(camera, null, widgetPrefName, cameraPrefName);
	}
	
	public CameraPanel(Camera camera, RotationDependAction.RotationProvider rotationProvider, String widgetPrefName, String cameraPrefName){
		super(Preferences.userRoot().node(widgetPrefName));
		this.camera = camera;
		this.rotationProvider = rotationProvider;
		widgetDefaults = Preferences.userRoot().node(widgetPrefName);
		cameraDefaults = Preferences.userRoot().node(cameraPrefName);
	}
	
	@Override
	protected void initStage(){
		super.initStage();
		stack.add(controlPanel());
	}

	protected Table controlPanel(){
		Table controlPanel = new Table();
		controlPanel.left().top();
		controlPanel.pad(10f);
		Label l = new Label("Camera control", Skinner.instance().skin(), "default");
		controlPanel.add(l).pad(5).top().left();
		controlPanel.row();
		
		CameraRollAction cameraRollAction = new CameraRollAction(camera, cameraDefaults);
		FlowTouchPad crp = FlowWidgetProvider.produceFlowTouchPad(cameraRollAction, "CameraRollPad", (int)(Math.min(width, height) * 0.7), widgetDefaults);
		controlPanel.add(crp).pad(3);

		CameraMoveAction cameraMoveAction;
		if(rotationProvider == null){
			cameraMoveAction = new CameraMoveAction(camera, cameraDefaults);
		}else{
			cameraMoveAction = new CameraMoveAction(camera, rotationProvider, cameraDefaults);
		}
		FlowTouchPad cmp = FlowWidgetProvider.produceFlowTouchPad(cameraMoveAction, "CameraMovePad", (int)(Math.min(width, height) * 0.7), widgetDefaults);
		controlPanel.add(cmp).pad(3);

		controlPanel.add(CameraAltitudeSlider.produceSlider((int)(Math.min(width, height) * 0.7), camera, cameraDefaults)).pad(3);		

		CameraVerticalRollAction cameraVerticalRollAction = new CameraVerticalRollAction(camera);
		HalfRTouchPad cameraVerticalRollPad = FlowWidgetProvider.produceFlowHalfRTouchPad(cameraVerticalRollAction, "CameraVerticalRoll", (int)(Math.min(width, height) * 0.7));
		controlPanel.add(cameraVerticalRollPad).pad(3);
		
		return controlPanel;
	}
	
}
