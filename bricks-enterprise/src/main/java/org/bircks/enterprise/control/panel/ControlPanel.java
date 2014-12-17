package org.bircks.enterprise.control.panel;

import java.util.prefs.Preferences;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public interface ControlPanel {
	
	public void setActive(boolean active);
	public boolean isActive();
	public void setPosition(float x, float y);
	public float getPositionX();
	public float getPositionY();
	public void resizeViewport(int width, int height);
	public void resize(float width, float height);
	public void draw(float deltaTime);
	public void applyManager(PanelManager pm);
	
	public void addAction(Action action);
	public void addListener(EventListener listener);
	
	public float getWidth();
	public float getHeight();
	
	public void show();
	public void hide();
}
