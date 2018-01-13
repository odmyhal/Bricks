package org.bircks.enterprise.control.panel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;

public class InteractiveController implements Disposable{

	public static final Preferences panelDefaults = Preferences.userRoot().node("panel.defaults");
	public static final Preferences cameraDefaults = Preferences.userRoot().node("camera.defaults");
	static{
		try {
			Preferences.importPreferences(InteractiveController.class.getResourceAsStream("/data/widget-defaults.xml"));
			Preferences.importPreferences(InteractiveController.class.getResourceAsStream("/data/config-defaults.xml"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("camera test " + cameraDefaults.getFloat("camera.move.maxRadius", 1f) + " = 2500.0");
	}


	private final List<ControlPanel> panels = new ArrayList<ControlPanel>(16);
	private final InputMultiplexer multiInputProcessor = new InputMultiplexer();
	private final ChangePanelListener changePanelListener = new ChangePanelListener();
	private final InputMultiplexer panelsInputProcessor = new InputMultiplexer();
//	private ControlPanel activePanel;
	
	public InteractiveController(){
		multiInputProcessor.addProcessor(0, panelsInputProcessor);
		multiInputProcessor.addProcessor(1, changePanelListener);
		Gdx.input.setInputProcessor(multiInputProcessor);
	}
	
	public InteractiveController(InputProcessor gameProcessor){
		this();
		multiInputProcessor.addProcessor(2, gameProcessor);
	}
	
	public void addPanel(ControlPanel panel){
		panel.applyManager(this);
		panels.add(panel);
	}
	
	public InputMultiplexer panelsMultiplexer(){
		return panelsInputProcessor;
	}
	
	public void render(float deltaTime){
		for(int i=0; i<panels.size(); i++){
			ControlPanel panel = panels.get(i);
			if(panel.isActive()){
				panel.draw(deltaTime);
			}
		}
	}

	public void resizeViewport(int width, int height){
		for(int i=0; i<panels.size(); i++){
			panels.get(i).resizeViewport(width, height);
		}
		changePanelListener.viewportWidth = width;
	}
	
	private class ChangePanelListener extends InputAdapter{
		
		private int[] pointers = new int[16];
		private float viewportWidth;
		
		@Override
		public boolean touchDown (int x, int y, int pointer, int button) {
			pointers[pointer] = x;
			return false;
		}
		
		
		public boolean touchDragged (int x, int y, int pointer) {
			int diffX = x - pointers[pointer];
//			System.out.println("diff is " + diffX + " , width is " + viewportWidth);
			if(Math.abs(diffX) > viewportWidth * 0.7){
				for(int i = 0; i< panels.size(); i++){
					ControlPanel activePanel = panels.get(i);
					if(activePanel.isActive() && activePanel.hide()){
						ControlPanel nextPanel = nextPanel(activePanel);
						boolean visible = nextPanel.show();
						if(!visible){
							throw new RuntimeException("Next panel can't be viewed");
						}
						break;
					}
				}
				return true;
			}
			return false;
		}
		
		private ControlPanel nextPanel(ControlPanel activePanel){
			if(panels.size() < 2){
				return null;
			}
			for(int i=0; i<panels.size() - 1; i++){
				if(activePanel == panels.get(i)){
					return panels.get(i + 1);
				}
			}
			return panels.get(0);
		}
	}

	public void dispose() {
		for(int i=0; i<panels.size(); i++){
			panels.get(i).dispose();
		}
	}
}
