package org.bircks.enterprise.control.panel.camera;

import java.util.prefs.Preferences;

import org.bricks.enterprise.control.widget.draw.DrawableRoll;
import org.bricks.enterprise.control.widget.tool.DoubleActionListener;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationConsumer;
import org.bricks.enterprise.d3.help.AlgebraUtils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraDrawableRollAction extends CameraRollAction{
	
	private static final float halfPI = (float) Math.PI / 2;
	
	private RotationConsumer drawableRoll;
	private DoubleActionListener myListener;

	public CameraDrawableRollAction(Camera camera, DrawableRoll drawableRoll, Preferences prefs) {
		super(camera, prefs);
		this.drawableRoll = drawableRoll;
	}
	
	public void setListener(DoubleActionListener dal){
		this.myListener = dal;
	}

	@Override
	public boolean act(float delta) {
		boolean res = super.act(delta);
		drawableRoll.consumeRotation((float) this.currentRotation() - halfPI);
		return res;
	}
	
	protected Vector2 calculateDirectionVector(FlowTouchPad widget){
		Vector2 myPosition = myListener.getPosition();
		touchPercentile.set(widget.getKnobX() - myPosition.x, widget.getKnobY() - myPosition.y);
		touchPercentile.nor();
		return touchPercentile;
	}

}
