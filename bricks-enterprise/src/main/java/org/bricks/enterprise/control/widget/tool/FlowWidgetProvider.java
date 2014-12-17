package org.bricks.enterprise.control.widget.tool;

import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.Skinner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class FlowWidgetProvider {

	private static final Preferences defaults = Preferences.userRoot().node("touchpad.defaults");
	static{
		try {
			Preferences.importPreferences(FlowWidgetProvider.class.getResourceAsStream("/data/widget-defaults.xml"));
		} catch (IOException e) {
			System.out.println("IO exception: " + e.getLocalizedMessage());
			Throwable t = e;
			while(t != null){
				System.out.println("------------------" + t.getMessage());
				t.printStackTrace();
				t = t.getCause();
			}
		} catch (InvalidPreferencesFormatException e) {
			System.out.println("Format exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static final <T> FlowTouchPad produceFlowTouchPad(FlowMutableAction<T, FlowTouchPad> action, String name, int radius){
		return produceFlowTouchPad(action, name, radius, defaults);
	}
	
	public static final <T> FlowTouchPad produceFlowTouchPad(FlowMutableAction<T, FlowTouchPad> action, String name, int radius, Preferences prefs){
		String padName = name + "-Backgorund-" + radius;
		int knobRadius = Math.max(10, radius / 13);
		String knobName = name + "-Knob-" + knobRadius;
		TouchpadStyle tps = new TouchpadStyle(produceBackground(padName, radius, prefs), produceKnob(knobName, knobRadius, prefs));
		
		FlowTouchListener<FlowTouchPad> listener = new FlowTouchListener<FlowTouchPad>(action);
		return new FlowTouchPad(tps, listener);
	}
	
	private static final Drawable produceBackground(String padName, int radius, Preferences prefs){
		if(!Skinner.instance().hasFrame(padName)){
			String bKey = "touchpad.background.color";
			Color background = Color.valueOf(prefs.get(bKey, defaults.get(bKey, "ffffffff")));
			String brKey = "touchpad.border.color";
			Color border = Color.valueOf(prefs.get(brKey, defaults.get(brKey, "ffffffff")));
			Skinner.instance().putFrame(radius, radius, 2, background, border, padName);
		}
		return Skinner.instance().skin().getDrawable(padName);
	}
	
	private static final Drawable produceKnob(String knobName, int kRadius, Preferences prefs){
		if(!Skinner.instance().hasFrame(knobName)){
			String bKey = "knob.background.color";
			Color background = Color.valueOf(prefs.get(bKey, defaults.get(bKey, "ffffffff")));
			Skinner.instance().putFrame(kRadius, kRadius, 0, background, background, knobName);
		}
		return Skinner.instance().skin().getDrawable(knobName);
	}
}
