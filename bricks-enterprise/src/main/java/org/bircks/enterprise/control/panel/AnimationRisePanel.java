package org.bircks.enterprise.control.panel;

import java.util.prefs.Preferences;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;

public class AnimationRisePanel extends RelativePanel{
	
	private static final float moveTime = 0.5f;
	private boolean canAct = true;
	private ShowAction showAction = new ShowAction();
	private HideAction hideAction = new HideAction();

	public AnimationRisePanel(){
		super();
	}
	
	public AnimationRisePanel(Preferences prefs){
		super(prefs);
	}
	
	public void show(){
		if(canAct){
			setPosition(-getWidth(), 0);
			this.setActive(true);
			showAction.setSpeed(getWidth() / moveTime);
			this.addAction(showAction);
			canAct = false;
		}
	};
	
	public void hide(){
		if(canAct){
			hideAction.setSpeed(getWidth() / moveTime);
			this.addAction(hideAction);
			canAct = false;
		}
	};
	
	private class ShowAction extends Action{
		
		private float moveSpeed;
		
		private void setSpeed(float mSpeed){
			moveSpeed = mSpeed;
		}

		@Override
		public boolean act(float delta) {
			boolean stopAction = false;
			float newPositionX = delta * moveSpeed + AnimationRisePanel.this.getPositionX();
			if(newPositionX >= 0){
				stopAction = true;
				AnimationRisePanel.this.inputControl();
				newPositionX = 0f;
				canAct = true;
			}
			AnimationRisePanel.this.setPosition(newPositionX, 0f);
			return stopAction;
		}
	}
	
	private class HideAction extends Action{
		private float moveSpeed;
		
		private void setSpeed(float mSpeed){
			moveSpeed = mSpeed;
		}

		@Override
		public boolean act(float delta) {
			boolean stopAction = false;
			float newPositionX = delta * moveSpeed + AnimationRisePanel.this.getPositionX();
			if(newPositionX >= AnimationRisePanel.this.getWidth()){
				stopAction = true;
				AnimationRisePanel.this.setActive(false);
				canAct = true;
				newPositionX = AnimationRisePanel.this.getWidth();
			}
			AnimationRisePanel.this.setPosition(newPositionX, 0f);
			return stopAction;
		}
	}

}
