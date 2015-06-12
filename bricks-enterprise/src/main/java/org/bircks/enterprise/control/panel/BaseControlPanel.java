package org.bircks.enterprise.control.panel;

import com.badlogic.gdx.math.Vector2;

public abstract class BaseControlPanel extends StagePanel{
	
	protected float width, height;
	private Vector2 position = new Vector2(0f, 0f);

	public void setPosition(float x, float y){
		position.set(x, y);
	}
	
	public float getPositionX(){
		return position.x;
	}
	
	public float getPositionY(){
		return position.y;
	}
	
	protected void init(float width, float height){
		this.width = width;
		this.height = height;
	}

	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}
