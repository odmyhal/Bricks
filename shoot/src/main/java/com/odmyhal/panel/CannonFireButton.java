package com.odmyhal.panel;

import org.bricks.extent.event.FireEvent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.odmyha.weapon.Cannon;

public class CannonFireButton extends TextButton{
	
	private Cannon cannon;

	private static final TextButtonStyle textButtonStyle = new TextButtonStyle();
	static {
		textButtonStyle.font = new BitmapFont();
		textButtonStyle.fontColor = Color.ORANGE;
	}

	public CannonFireButton(String text, Cannon cann){
		super(text,  textButtonStyle);
		this.cannon = cann;
		this.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				cannon.addEvent(new FireEvent());
			}
		});
	}
}
