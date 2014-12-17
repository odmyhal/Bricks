package org.bircks.enterprise.control.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StackPanel extends BaseControlPanel{

	
	protected final Stack stack = new Stack();
	private Table layer;
	
	
	protected int padding;
	protected Color background, border;
	
	{
		stage.addActor(stack);
	}
	
	protected void init(int width, int height, int padding, Color background, Color border){
		init(width, height);
		this.padding = padding;
		this.background = background;
		this.border = border;
	}
	
	protected void initStage(){
		stack.clear();
		stack.setWidth(width);
		stack.setHeight(height);
		layer = new Table();
		Image im = new Image(Skinner.instance().getFrame((int)width, (int)height, padding, background, border));
		
		layer.add(im).expand();
		stack.add(layer);
	}
	
	public void resize(float width, float height){
		init(width, height);
		initStage();
	}

	@Override
	public void setPosition(float x, float y){
		super.setPosition(x, y);
		stack.setPosition(x, y);
//		stack.setOrigin(x, y);
//		stage.stageToScreenCoordinates(getPosition());
		
	}

}
