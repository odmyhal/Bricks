package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;

public class FlowSlider extends Slider{
	
	public FlowSlider(float minVal, float maxVal, boolean vertical, SliderStyle style, FlowTouchListener listener){
		super(minVal, maxVal, 1, vertical, style);
		this.addFlowTouchListener(listener);
	}

	private void addFlowTouchListener(FlowTouchListener listener){
		listener.setWidget(this);
		addListener(listener);
	}
}
