package org.bircks.enterprise.control.panel;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StagePanel implements ControlPanel{


	private boolean active;
	protected final Stage stage = new Stage();
	private InputMultiplexer panelsMultiplexer;
	
	public void resizeViewport(int width, int height) {
		Viewport viewport = stage.getViewport();
		viewport.setWorldWidth(width);
		viewport.setWorldHeight(height);
		viewport.update(width,  height, true);
	}
	

	public void setActive(boolean active){
		this.active = active;
		if(active){
			panelsMultiplexer.addProcessor(stage);
		}else{
			panelsMultiplexer.removeProcessor(stage);
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	
	public void draw(float deltaTime) {
		stage.act(deltaTime);
	    stage.draw();
	}

	public void addAction(Action action){
		stage.addAction(action);
	}

	public boolean show(){
		setActive(true);
		return true;
	};
	
	public boolean hide(){
		setActive(false);
		return true;
	};
	
	public void applyManager(InteractiveController pm) {
		this.panelsMultiplexer = pm.panelsMultiplexer();
	}
}
