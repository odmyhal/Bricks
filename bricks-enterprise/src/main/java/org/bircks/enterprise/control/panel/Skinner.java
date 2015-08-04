package org.bircks.enterprise.control.panel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Skinner {
	
	private final Skin skin = new Skin();
	private static final Skinner instance = new Skinner();
	
	private Skinner(){
		LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.RED);
		skin.add("default", labelStyle);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = new BitmapFont();
		textButtonStyle.fontColor = Color.ORANGE;
		skin.add("fire", textButtonStyle);
	};
	
	public Texture getFrame(int width, int height, int padding, Color background, Color border){
		String pName = String.format("panel_frame_w:%d_h:%d_p:%d_bg:%.2f-%.2f-%.2f-%.2f_br:%.2f-%.2f-%.2f-%.2f",
				width, height, padding,
				background.r, background.g, background.b, background.a,
				border.r, border.g, border.b, border.a);
		if(!skin.has(pName, Texture.class)){
			skin.add(pName, produceFrame(width, height, padding, background, border));
		}
		return skin.get(pName, Texture.class);
	}
	
	public void putFrame(int width, int height, int padding, Color background, Color border, String fName){
		skin.add(fName, produceFrame(width, height, padding, background, border));
	}
/*	
	public void putFrame(int width, int height, int padding, Color background, Color border, String fName, Class cls){
		skin.add(fName, produceFrame(width, height, padding, background, border), cls);
	}
*/	
	public boolean hasFrame(String name){
		return skin.has(name, Texture.class);
	}
	
	private Texture produceFrame(int width, int height, int padding, Color background, Color border){
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(background);
		pixmap.fillRectangle(padding, padding, width - padding * 2, height - padding * 2);
		pixmap.setColor(border);
		pixmap.drawRectangle(padding, padding, width - padding * 2, height - padding * 2);
		return new Texture(pixmap);
	}
	
	public Texture produceFrameWithText(int width, int height, int padding, Color background, Color border,
			int textX, int textY, Color textColor, String text){
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(background);
		pixmap.fillRectangle(padding, padding, width - padding * 2, height - padding * 2);
		pixmap.setColor(border);
		pixmap.drawRectangle(padding, padding, width - padding * 2, height - padding * 2);
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		return texture;
	}

	public Skin skin(){
		return skin;
	}
	
	public static Skinner instance(){
		return instance;
	}
}
