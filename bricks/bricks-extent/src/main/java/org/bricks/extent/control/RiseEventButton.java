package org.bricks.extent.control;


import org.bricks.engine.event.Event;
import org.bricks.engine.staff.Liver;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class RiseEventButton extends TextButton{
	
	private Liver<?> target;

	public RiseEventButton(Liver<?> liver, String text) {
		this(liver, text, provideStyle());
	}
	
	public RiseEventButton(Liver<?> liver, String text, TextButtonStyle style) {
		super(text, style);
		this.target = liver;
		this.addListener(new ClickListener(){
			public void clicked (InputEvent e, float x, float y) {
				RiseEventButton.this.target.addEvent(RiseEventButton.this.provideEvent());
			}
		});
	}
	
	protected abstract Event provideEvent();

	protected static TextButtonStyle provideStyle(){
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.font = new BitmapFont();
		tbs.fontColor = Color.ORANGE;
		return tbs;
	}
}

