package org.bircks.enterprise.control.panel;

import java.util.prefs.Preferences;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;

public interface ControlPanel extends Disposable{
	
	public void setActive(boolean active);
	public boolean isActive();
	public void setPosition(float x, float y);
	public float getPositionX();
	public float getPositionY();
	public void resizeViewport(int width, int height);
	public void resize(float width, float height);
	public void draw(float deltaTime);
	public void applyManager(InteractiveController pm);
	
	public void addAction(Action action);
//	public void addListener(EventListener listener);
	
	public float getWidth();
	public float getHeight();
	
	public boolean show();
	public boolean hide();
//	public InputProcessor inputProcessor();
//	public void setMultiplexer(InputMultiplexer inputMultiplexer)
}
