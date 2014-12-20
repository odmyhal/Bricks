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
//		System.out.println(this.getClass().getCanonicalName() + " draw called");
//		System.out.println(String.format("x = %.2f, y = %.2f, width = %.2f, height = %.2f, originX = %.2f, originY = %.2f", x, y, width, height, x + width / 2, y + height / 2));
		this.draw(batch, x, y, width / 2, height / 2, width, height, 1f, 1f, rotation);
	}
/*	
	public void setWidth(int width){
		this.myWidth = width;
	}
	
	public void setHeight(int height){
		this.myHeight = height;
	}
	*/
}
