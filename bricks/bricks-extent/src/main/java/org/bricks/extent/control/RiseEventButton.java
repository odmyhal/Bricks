package org.bricks.extent.control;

import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RiseEventButton extends TextButton{
	
	private Liver<?> target;
	private Event event;

	public RiseEventButton(Liver<?> liver, Event evnt, String text) {
		super(text, provideStyle());
		this.target = liver;
		this.event = evnt;
		this.addListener(new ClickListener(){
			public void clicked (InputEvent e, float x, float y) {
				RiseEventButton.this.target.addEvent(RiseEventButton.this.event);
			}
		});
	}

	protected static TextButtonStyle provideStyle(){
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.font = new BitmapFont();
		tbs.fontColor = Color.ORANGE;
		return tbs;
	}
}
