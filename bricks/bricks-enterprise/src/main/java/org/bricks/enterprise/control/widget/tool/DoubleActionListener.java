package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class DoubleActionListener<W extends Widget> extends FlowTouchListener<W>{

//	private static float stayDiff = 5f;
	
	protected FlowMutableAction<?, W> drugAction;
	private Vector2[] pointers = new Vector2[5];
	
	private InputListener nativeListener;
	private Vector2 oldPosition = new Vector2();
	private Vector2 knobPersents = new Vector2();
	private float stayDiff;
	
	public DoubleActionListener(FlowMutableAction<?, W> touchAction, FlowMutableAction<?, W> drugAction, float inchDrug) {
		super(touchAction);
		this.drugAction = drugAction;
		for(int i = 0; i < pointers.length; i++){
			pointers[i] = new Vector2();
		}
		stayDiff = inchDrug * Gdx.graphics.getDensity() * 160;
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
	
	public void rememberKnobPersent(float persentX, float persentY){
		knobPersents.set(persentX, persentY);
	}
	
	public void tuneKnobAfterResize(float width, float height){
		float xPos = width * (knobPersents.x + 1) / 2;
		float yPos = height * (knobPersents.y + 1) / 2;
//		System.out.println("TuneKnob after resize persents: " + knobPersents + ", width: " + width + ", height: " + height);
		nativeListener.touchDown(null, xPos, yPos, 0, 0);
		nativeListener.touchUp(null, xPos, yPos, 0, 0);
		oldPosition.set(xPos, yPos);
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
//			System.out.println("Touch drugged: " + x + ", " + y); 
			tuneAction(this.drugAction);
			oldPosition.set(x, y);
		}
	}
	
	public void simulateDrug(float x, float y){
		nativeListener.touchDown(null, x + stayDiff + 1, y + stayDiff + 1, 0, 0);
		touchDown(null, x + stayDiff + 1, y + stayDiff + 1, 0, 0);
		nativeListener.touchDragged(null, x, y, 0);
		touchDragged(null, x, y, 0);
		nativeListener.touchUp(null, x, y, 0, 0);
		touchUp(null, x, y, 0, 0);
	}
}
