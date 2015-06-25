package org.bricks.extent.processor;

import java.util.ArrayList;

import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.item.MultiLiver;
import org.bricks.engine.processor.WorkToConditionProcessor;
import org.bricks.engine.staff.AvareTimer;
import org.bricks.extent.space.Walk3D;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class TranslateModelBrickProcessor <T extends MultiLiver<? extends ModelBrickSubject, ?, ?>>
	extends WorkToConditionProcessor<T> implements AvareTimer{
	
	private static final  float minDiff = 0.7f;
	
	private Walk3D walk = new Walk3D();
	private volatile long time;
	private Vector3 translation, leftTranslation = new Vector3(), speed = new Vector3();
	private boolean unchanged;
	
	public TranslateModelBrickProcessor(){
		super(CheckerType.NO_SUPLANT);
	}
	public TranslateModelBrickProcessor(CheckerType type) {
		super(type);
	}
	
	public void init(Vector3 translation, long timeMills){
		this.translation = translation;
		time = timeMills;
	}
	
	@Override
	public void activate(T target, long curTime){
		float timeK = 1000f / time;
		speed.set(translation.x * timeK, translation.y * timeK, translation.z * timeK);
		leftTranslation.set(translation);
//		walk.flushTimer(curTime);
		walk.timerSet(curTime);
		unchanged = false;
//		Gdx.app.debug("MESSAGE", "Activated with speed " + speed + ", after leftRranslation: " + leftTranslation);
		super.activate(target, curTime);
	}
	

	public void timerSet(long time){
		walk.timerSet(time);
	}
	
	public void timerAdd(long time){
		walk.timerAdd(time);
	}

	@Override
	public void doJob(T target, long processTime) {
		if(walk.move(processTime, speed)){
			Vector3 movement = walk.lastMove().source;
			moveStaff(target, movement);
			leftTranslation.sub(movement);
			unchanged = false;
//			Gdx.app.debug("MESSAGE", "did movement " + movement + " with speed " + speed + ", after leftRranslation: " + leftTranslation);
		}
	}

	@Override
	public boolean stopCondition(T target, long processTime) {
		if(unchanged){
			return false;
		}
		if(finish()){
			moveStaff(target, leftTranslation);
			return true;
		}
		unchanged = true;
		return false;
	}
	
	private boolean finish(){
		return leftTranslation.x * speed.x < 0 
				|| leftTranslation.y * speed.y < 0  
				|| leftTranslation.z * speed.z < 0 ;
	}

	private void moveStaff(T target, Vector3 movement){
		ArrayList<? extends ModelBrickSubject> staff = target.getStaff();
		for(int i = 0; i < staff.size(); i++){
			staff.get(i).linkModelBrick().translate(movement);
		}
		target.setUpdate();
	}
	
}
