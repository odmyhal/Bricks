package org.bricks.enterprise.control.widget.draw;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CompoundDrawable extends BaseDrawable{
	
	Collection<Drawable> data = new LinkedList<Drawable>();
	
	public CompoundDrawable(Drawable... drawables){
		this.data.addAll(Arrays.asList(drawables));
	}

	public void draw (Batch batch, float x, float y, float width, float height) {
		for(Drawable drawable : data){
			drawable.draw(batch, x, y, width, height);
		}
	}
}
