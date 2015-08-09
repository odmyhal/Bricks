package org.bricks.extent.entity;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.engine.item.MultiWalker;
import org.bricks.engine.neve.WalkPrint;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Roll;
import org.bricks.enterprise.control.widget.tool.RotationDependAction.RotationProvider;
import org.bricks.exception.Validate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

public class CameraSatellite implements Satellite<Point, Roll>, RotationProvider{
	
	public Camera camera;
//	private MultiWalker target;
	
	private static final int cap = 256;
	private final int[] trX = new int[cap], trY = new int[cap], q = new int[cap], orgX = new int[cap], orgY = new int[cap];
	private final float[] rotate = new float[cap];//, rotation = new float[cap];
	
	private volatile int curIndex = 0;
	private int lastIndex = 0;
	
	private float generalRotation;
	private boolean needUpdate = true;

	Fpoint tmp = new Fpoint(0f, 0f);
	
	public CameraSatellite(Camera camera, float startRotation/*MultiWalker target*/){
		this.camera = camera;
		generalRotation = startRotation;
	}

	/**
	 * Method called in motor thread
	 */
//	public volatile int rotateIndex = 0;
//	public float rotatedRotation = 0;
//	public float updatedRotation = 0;
	public void rotate(Roll roll, Origin<Point> central){
		q[curIndex] = 1;
		rotate[curIndex] += roll.lastRotation();
		orgX[curIndex] = central.source.getX();
		orgY[curIndex] = central.source.getY();
		generalRotation = roll.getRotation();
//		rotatedRotation += roll.lastRotation();
//		++rotateIndex;
	}

	/**
	 * Method called in motor thread
	 */
	public void translate(Origin<Point> trn) {
		q[curIndex] = 2;
		trX[curIndex] += trn.source.getX();
		trY[curIndex] += trn.source.getY();
	}

	/**
	 * Method called in motor thread
	 */
//	public volatile int updateIndex = 0;
	public void update() {
		int nextIndex = (curIndex + 1) % cap;
		trX[nextIndex] = trY[nextIndex] = 0;
		rotate[nextIndex] = 0f;
		curIndex = nextIndex;
//		++updateIndex;
	}

	/**
	 * Method called in render thread
	 */
//	public int applyCounter = 0;
//	public int trnApply = 0;
	public void applyUpdates(){
		int index = curIndex;
		if(lastIndex != index){
			int translateX = 0;
			int translateY = 0;
			do{
//				Validate.isFalse(q[lastIndex] == 0, "Unpdate indentificator should equal 1 or 2 here");
				if(q[lastIndex] == 0){
					Gdx.app.debug("WARNING", "CameraSetellite called update without any transformations...(see MultiLiver, line:38, needUpdate = true)");
				}else if(rotate[lastIndex] == 0){
					translateX += trX[lastIndex];
					translateY += trY[lastIndex];
//					++trnApply;
				}else{
//					generalRotation = rotation[lastIndex];
					if( (trX[lastIndex] == 0 && trY[lastIndex] == 0) || q[lastIndex] == 2 ){
						this.camera.translate(translateX, translateY, 0f);
						rotateCamera(rotate[lastIndex], orgX[lastIndex], orgY[lastIndex]);
//						updatedRotation += rotate[lastIndex];
//						++applyCounter;
						translateX = trX[lastIndex];
						translateY = trY[lastIndex];
					}else{
						this.camera.translate(translateX + trX[lastIndex], translateY + trY[lastIndex], 0f);
						rotateCamera(rotate[lastIndex], orgX[lastIndex], orgY[lastIndex]);
//						updatedRotation += rotate[lastIndex];
//						++applyCounter;
						translateX = 0;
						translateY = 0;
					}
				}
				lastIndex = (lastIndex + 1) % cap;
			}while(lastIndex != index);
			this.camera.translate(translateX, translateY, 0f);
			this.camera.update();
		}
		needUpdate = false;

//		System.out.println("Applying " + updateIndex + ", " + rotateIndex + ", applyed: " + applyCounter + ", appTrn " + trnApply + " || rotated: " + rotatedRotation + ", updated " + updatedRotation);
	}
	
	public void checkUpdate(){
		if(needUpdate){
			applyUpdates();
		}else{
			needUpdate = true;
		}
	}
	
	private void rotateCamera(float radians, float centerX, float centerY){
		this.camera.rotate((float) Math.toDegrees(radians), 0f, 0f, 99f);
		float dx = camera.position.x - centerX;
		float dy = camera.position.y - centerY;
		tmp.setX(dx);
		tmp.setY(dy);
		PointHelper.rotatePointByZero(tmp, Math.sin(radians), Math.cos(radians), tmp);
		camera.position.x += tmp.x - dx;
		camera.position.y += tmp.y - dy;
	}

	public float provideRotation() {
		return generalRotation;
	}
	
}
