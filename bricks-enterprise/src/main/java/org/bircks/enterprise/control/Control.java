package org.bircks.enterprise.control;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Control extends InputListener{

	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//		System.out.println(String.format("Knob date percent - %.2f : %.2f, absolute - %.2f : %.2f", getKnobPercentX(), getKnobPercentY(), getKnobX(), getKnobY()));

		return true;
	}
	
	@Override
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//		System.out.println(String.format("Knob position " + getKnobX() + " : " + getKnobY()));
	}
	
	public void act(float deltaTime){
		throw new RuntimeException("This method should be overriden");
	}
}
