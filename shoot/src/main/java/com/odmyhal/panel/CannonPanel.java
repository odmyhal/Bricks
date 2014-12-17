package com.odmyhal.panel;

import org.bircks.enterprise.control.panel.AnimationRisePanel;
import org.bircks.enterprise.control.panel.Skinner;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.control.widget.tool.FlowWidgetProvider;
//import org.bricks.extent.control.MarkRollAction;
import org.bricks.extent.control.RollEntityAction;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.odmyha.weapon.Cannon;

public class CannonPanel extends AnimationRisePanel{

private Cannon cannon;
	
	public CannonPanel(Cannon cannon){
		this.cannon = cannon;
	}
	
	@Override
	protected void initStage(){
		super.initStage();
		stack.add(controlPanel());
	}

	private Table controlPanel(){
		Table controlPanel = new Table();
		controlPanel.left().top();
		controlPanel.pad(10f);
		Label l = new Label("Cannon panel", Skinner.instance().skin(), "default");
		controlPanel.add(l).pad(5).top().left();
		
//		MarkRollAction<Cannon, FlowTouchPad> cannonRollAction = new MarkRollAction<Cannon, FlowTouchPad>(cannon, 0.9f);
		RollEntityAction cannonRollAction = new RollEntityAction(cannon, 0.9f);
		FlowTouchPad ftp = FlowWidgetProvider.produceFlowTouchPad(cannonRollAction, "CannonRollPad", (int)(Math.min(width, height) * 0.7));
		controlPanel.add(ftp).pad(3);
		controlPanel.add(new CannonFireButton("FIRE", cannon)).pad(8);
		return controlPanel;
	}
	
}
