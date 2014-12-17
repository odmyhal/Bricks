package org.bircks.enterprise.control.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StagePanel implements ControlPanel{

	protected final Stage stage = new Stage();
	
	
	public void resizeViewport(int width, int height) {
		Viewport viewport = stage.getViewport();
		viewport.setWorldWidth(width);
		viewport.setWorldHeight(height);
		viewport.update(width,  height, true);
	}
	
	public void draw(float deltaTime) {
		stage.act(deltaTime);
	    stage.draw();
	}
	

	public void addListener(EventListener listener) {
		stage.addListener(listener);
	}
	
	public void addAction(Action action){
		stage.addAction(action);
	}
	
	public void inputControl(){
		Gdx.input.setInputProcessor(stage);
	}
	

	public void show(){
		inputControl();
		setActive(true);
	};
	
	public void hide(){
		setActive(false);
	};
}
