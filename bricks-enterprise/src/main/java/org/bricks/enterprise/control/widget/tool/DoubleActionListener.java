package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class DoubleActionListener<W extends Widget> extends FlowTouchListener<W>{

	private static float stayDiff = 5f;
	
	protected FlowMutableAction<?, W> drugAction;
	private Vector2[] pointers = new Vector2[5];
	
	private InputListener nativeListener;
	private Vector2 oldPosition = new Vector2();
	
	public DoubleActionListener(FlowMutableAction<?, W> touchAction, FlowMutableAction<?, W> drugAction) {
		super(touchAction);
		this.drugAction = drugAction;
		for(int i = 0; i < pointers.length; i++){
			pointers[i] = new Vector2();
		}
	}
	
	public void setNativeListener(InputListener nativ){
		this.nativeListener = nativ;
	}
	
	public void setPosition(float x, float y){
		oldPosition.set(x, y);
	}
	
	public Vector2 getPosition(){
		return oldPosition;
	}

	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		if(pointer < pointers.length){
			pointers[pointer].set(x, y);
//			System.out.println("Old position is: " + oldPosition);
			nativeListener.touchUp(null, oldPosition.x, oldPosition.y, pointer, button);
			return true;
		}
		return false;
	}
	
	private boolean isDrug(int pointer, float x, float y){
		return Math.abs(pointers[pointer].x - x) > stayDiff || Math.abs(pointers[pointer].y - y) > stayDiff; 
	}
	
	@Override
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		if(isDrug(pointer, x, y)){
			return;
		}
		tuneAction(this.flowAction);
		nativeListener.touchUp(null, oldPosition.x, oldPosition.y, pointer, button);
	}
	
	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		if(isDrug(pointer, x, y)){
			tuneAction(this.drugAction);
			oldPosition.set(x, y);
		}
	}
}
