package org.bricks.enterprise.control.widget.draw;

import org.bricks.enterprise.control.widget.tool.RotationDependAction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DrawableRoll extends TextureRegionDrawable implements RotationDependAction.RotationConsumer{

	private float rotation;
//	private int myWidth, myHeight;
	
	public DrawableRoll(TextureRegion region){
		super(region);
//		myWidth = width;
//		myHeight = height;
	}
	
	/**
	 * Consumers rotation in radians and convert it into angles
	 */
	public void consumeRotation(float rotation) {
		this.rotation = (float) Math.toDegrees(rotation);
	}

	@Override
	public void draw (Batch batch, float x, float y, float width, float height) {
		this.draw(batch, x, y, width / 2, height / 2, width, height, 1f, 1f, rotation);
	}
}
