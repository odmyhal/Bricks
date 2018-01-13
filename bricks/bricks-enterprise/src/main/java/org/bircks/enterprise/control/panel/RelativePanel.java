package org.bircks.enterprise.control.panel;

import java.util.prefs.Preferences;

import com.badlogic.gdx.graphics.Color;

public class RelativePanel extends StackPanel {
	
	private float rWidth, rHeight;

	public RelativePanel(){
		this(InteractiveController.panelDefaults);
	}
	
	public RelativePanel(Preferences prefs){
		initRatio(prefs);
//		System.out.println(this.getClass().getCanonicalName() + ", rWidth: " + rWidth + ", rHeight: " + rHeight);
		int rPadding = prefs.getInt("panel.padding", InteractiveController.panelDefaults.getInt("panel.padding", 1));
		Color rBackground = Color.valueOf(prefs.get("panel.background.color", InteractiveController.panelDefaults.get("panel.background.color", "ffffffff")));
		Color rBorder = Color.valueOf(prefs.get("panel.border.color", InteractiveController.panelDefaults.get("panel.border.color", "ffffffff")));
		init(0, 0, rPadding, rBackground, rBorder);
	}

	public void resizeViewport(int width, int height) {
		int w = (int) Math.round(width * rWidth);
		int h = (int) Math.round(height * rHeight);
		super.resizeViewport(width, height);
		resize(w, h);
	}
	
	protected void initRatio(Preferences prefs){
		this.rWidth = prefs.getFloat("panel.relative.width", InteractiveController.panelDefaults.getFloat("panel.relative.width", 1f));
		this.rHeight = prefs.getFloat("panel.relative.height", InteractiveController.panelDefaults.getFloat("panel.relative.height", 0.5f));
	}
/*
	public void applyManager(PanelManager pm) {
		// TODO Auto-generated method stub
		
	}
*/
}
