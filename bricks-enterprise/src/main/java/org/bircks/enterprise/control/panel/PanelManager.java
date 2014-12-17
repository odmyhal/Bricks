package org.bircks.enterprise.control.panel;

import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class PanelManager {

	public static final Preferences panelDefaults = Preferences.userRoot().node("panel.defaults");
	public static final Preferences cameraDefaults = Preferences.userRoot().node("camera.defaults");
	static{
		try {
			Preferences.importPreferences(PanelManager.class.getResourceAsStream("/data/widget-defaults.xml"));
			Preferences.importPreferences(PanelManager.class.getResourceAsStream("/data/config-defaults.xml"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		System.out.println("Panel test " + panelDefaults.getInt("panel.padding", 1) + " = 10");
//		System.out.println("camera test " + cameraDefaults.getFloat("camera.move.maxRadius", 1f) + " = 2500.0");
	}


	private final List<ControlPanel> panels = new LinkedList<ControlPanel>();
	
	public void addPanel(ControlPanel panel){
		panel.applyManager(this);
		panel.addListener(new ChangePanelListener(panel));
		panels.add(panel);
	}
	
	public void render(float deltaTime){
		for(ControlPanel panel: panels){
			if(panel.isActive()){
				panel.draw(deltaTime);
			}
		}
	}

	public void resizeViewport(int width, int height){
		for(ControlPanel panel: panels){
			panel.resizeViewport(width, height);
		}
	}
	
	private class ChangePanelListener extends InputListener{
		
		private ControlPanel panel;
		private float lastXDown;
		private ChangePanelListener(ControlPanel cp){
			this.panel = cp;
		}
		
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			lastXDown = x;
			return true;
		}
		
		public void touchDragged (InputEvent event, float x, float y, int pointer) {
			float diffX = x - lastXDown;
			if(Math.abs(diffX) > panel.getWidth() * 0.4){
				ControlPanel nextPanel = nextPanel();
				nextPanel.show();
				panel.hide();
			}
		}
		
		private ControlPanel nextPanel(){
			if(panels.size() < 2){
				return null;
			}
			for(int i=0; i<panels.size() - 1; i++){
				if(panel == panels.get(i)){
					return panels.get(i + 1);
				}
			}
			return panels.get(0);
		}
	}
}
