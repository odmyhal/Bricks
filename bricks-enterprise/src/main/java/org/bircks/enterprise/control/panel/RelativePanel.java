package org.bircks.enterprise.control.panel;

import java.util.prefs.Preferences;

import com.badlogic.gdx.graphics.Color;

public class RelativePanel extends StackPanel {
	
	private float rWidth, rHeight;

	public RelativePanel(){
		this(PanelManager.panelDefaults);
	}
	
	public RelativePanel(Preferences prefs){
		this.rWidth = prefs.getFloat("panel.relative.width", PanelManager.panelDefaults.getFloat("panel.relative.width", 1f));
		this.rHeight = prefs.getFloat("panel.relative.height", PanelManager.panelDefaults.getFloat("panel.relative.height", 0.5f));
		int rPadding = prefs.getInt("panel.padding", PanelManager.panelDefaults.getInt("panel.padding", 1));
		Color rBackground = Color.valueOf(prefs.get("panel.background.color", PanelManager.panelDefaults.get("panel.background.color", "ffffffff")));
		Color rBorder = Color.valueOf(prefs.get("panel.border.color", PanelManager.panelDefaults.get("panel.border.color", "ffffffff")));
		init(0, 0, rPadding, rBackground, rBorder);
	}

	public void resizeViewport(int width, int height) {
		int w = (int) Math.round(width * rWidth);
		int h = (int) Math.round(height * rHeight);
		super.resizeViewport(width, height);
		resize(w, h);
	}

	public void applyManager(PanelManager pm) {
		// TODO Auto-generated method stub
		
	}

}
