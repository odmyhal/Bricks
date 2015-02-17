package org.bricks.extent.entity;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.view.WalkView;
import org.bricks.enterprise.control.widget.tool.RotationDependAction;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.Camera;

public class CameraSatellite implements Satellite, RotationDependAction.RotationProvider{
	
	public Camera camera;
	private MultiWalker target;
	
	private static final int cap = 50;
	private final int[] trX = new int[cap], trY = new int[cap], q = new int[cap], orgX = new int[cap], orgY = new int[cap];
	private final float[] rotate = new float[cap];//, rotation = new float[cap];
	
	private volatile int curIndex = 0;
	private int lastIndex = 0;
	
	private float generalRotation;

	Fpoint tmp = new Fpoint(0f, 0f);
	
	public CameraSatellite(Camera camera, MultiWalker target){
		this.camera = camera;
		this.target = target;
		WalkPrint wv = (WalkPrint)target.getSafePrint();
		generalRotation = wv.getRotation();
		wv.free();
	}

	/**
	 * Method called in motor thread
	 */
	public void rotate(float rad, Point central) {
		q[curIndex] = 1;
		rotate[curIndex] = target.lastRotation();
		orgX[curIndex] = central.getX();
		orgY[curIndex] = central.getY();
//		rotation[curIndex] = target.getRotation();
		generalRotation = target.getRotation();
	}

	/**
	 * Method called in motor thread
	 */
	public void translate(int x, int y) {
		q[curIndex] = 2;
		trX[curIndex] = x;
		trY[curIndex] = y;
	}

	/**
	 * Method called in motor thread
	 */
	public void update() {
		int nextIndex = (curIndex + 1) % cap;
		trX[nextIndex] = trY[nextIndex] = 0;
		rotate[nextIndex] = 0f;
		curIndex = nextIndex;
	}

	/**
	 * Method called in render thread
	 */
	public void applyUpdates(){
		int index = curIndex;
		if(lastIndex != index){
			int translateX = 0;
			int translateY = 0;
			do{
				Validate.isFalse(q[lastIndex] == 0, "Unpdate indentificator should equal 1 or 2 here");
				if(rotate[lastIndex] == 0){
					translateX += trX[lastIndex];
					translateY += trY[lastIndex];
				}else{
//					generalRotation = rotation[lastIndex];
					if( (trX[lastIndex] == 0 && trY[lastIndex] == 0) || q[lastIndex] == 2 ){
						this.camera.translate(translateX, translateY, 0f);
						rotateCamera(rotate[lastIndex], orgX[lastIndex], orgY[lastIndex]);
						translateX = trX[lastIndex];
						translateY = trY[lastIndex];
					}else{
						this.camera.translate(translateX + trX[lastIndex], translateY + trY[lastIndex], 0f);
						rotateCamera(rotate[lastIndex], orgX[lastIndex], orgY[lastIndex]);
						translateX = 0;
						translateY = 0;
					}
				}
				lastIndex = (lastIndex + 1) % cap;
			}while(lastIndex != index);
			this.camera.translate(translateX, translateY, 0f);
			this.camera.update();
		}
	}
	
	private void rotateCamera(float radians, float centerX, float centerY){
		this.camera.rotate((float) Math.toDegrees(radians), 0f, 0f, 99f);
		float dx = camera.position.x - centerX;
		float dy = camera.position.y - centerY;
		tmp.setX(dx);
		tmp.setY(dy);
		PointHelper.rotatePointByZero(tmp, Math.sin(radians), Math.cos(radians), tmp);
		camera.position.x += tmp.getFX() - dx;
		camera.position.y += tmp.getFY() - dy;
	}

	public float provideRotation() {
		return generalRotation;
	}
	
}
