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
		this(liver, evnt, text, provideStyle());
	}
	
	public RiseEventButton(Liver<?> liver, Event evnt, String text, TextButtonStyle style) {
		super(text, style);
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
