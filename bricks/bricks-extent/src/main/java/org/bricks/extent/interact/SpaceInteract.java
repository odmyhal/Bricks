package org.bricks.extent.interact;

import java.util.Arrays;
import java.util.HashSet;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.item.Stone;
import org.bricks.engine.pool.Area;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Subject;
import org.bricks.exception.Validate;
import org.bricks.extent.interact.InteractiveHandler.Interactive;
import org.bricks.extent.processor.tbroll.Butt;
import org.bricks.extent.processor.tbroll.Vector3Butt;
import org.bricks.extent.space.overlap.LineCrossMBAlgorithm;
import org.bricks.extent.space.overlap.MeshLineCrossAlgorithm;
import org.bricks.extent.subject.model.MBPrint;
import org.bricks.extent.subject.model.ModelBrickSubject;
import org.bricks.extent.subject.model.ModelBrick;
import org.bricks.utils.Cache;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class SpaceInteract extends InputAdapter{
	
	private Camera camera;
	private World<ModelBrickSubject> world;
	private static final int poolSize = 64;
	private static final double tapPercentPreciseK = 2d / 100d;
	private int[] touchPool = new int[poolSize << 1];
	private HashSet<Entity> checkedEntities = new HashSet<Entity>();
	private final MeshLineCrossAlgorithm<?, ?> lsAlgorithm = new LineCrossMBAlgorithm();
	private float[] lenK = new float[4];
	private DefaultHandler defaultHandler= null;
//	private Butt activeButt;
	
	private Fpoint startHPoint = new Fpoint(), endHPoint = new Fpoint();
	
	private static final SpaceInteract instance = new SpaceInteract();
	
	private SpaceInteract(){
		
	}
	
	public static final void init(Camera camera, World world, DefaultHandler df){
		init(camera, world);
		setDefaultHandler(df);
	}
	
	public static final void init(Camera camera, World world){
		instance.camera = camera;
		instance.world = world;
	}
	
	public static final void setDefaultHandler(DefaultHandler df){
		instance.defaultHandler = df;
	}
/*	
	public static final Butt activeButt(){
		return instance.activeButt;
	}
*/	
	public static final SpaceInteract instance(){
		return instance;
	}

	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		setTouch(pointer, screenX, screenY);
		return true;
	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		int dx = Math.abs(poolX(pointer) - screenX);
		int dy = Math.abs(poolY(pointer) - screenY);
		if(dx < camera.viewportWidth * tapPercentPreciseK
				&& dy < camera.viewportHeight * tapPercentPreciseK){
			tap(screenX, screenY);
		}
		return true;
	}

	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return true;
	}

	private void setTouch(int pointer, int x, int y){
		Validate.isTrue(pointer < poolSize, "TouchPool is too small, need to extent");
		touchPool[pointer] = x;
		touchPool[pointer + poolSize] = y;
	}
	
	private int poolX(int pointer){
		return touchPool[pointer];
	}
	
	private int poolY(int pointer){
		return touchPool[pointer + poolSize];
	}
	
	private void tap(int screenX, int screenY){
		Ray ray = camera.getPickRay(screenX, screenY);
		startHPoint.x = ray.origin.x;
		startHPoint.y = ray.origin.y;
		endHPoint.x = ray.origin.x + ray.direction.x * camera.far;
		endHPoint.y = ray.origin.y + ray.direction.y * camera.far;
		
		int startPointRow = world.detectSectorRow(startHPoint);
		int startPointCol = world.detectSectorCol(startHPoint);
		int endPointRow = world.detectSectorRow(endHPoint);
		int endPointCol = world.detectSectorCol(endHPoint);
		
		int rowStep = endPointRow > startPointRow ? 1 : -1;
		int colStep = endPointCol > startPointCol ? 1 : -1;
		
		int i = startPointRow;
		Vector3 lastMove = Cache.get(Vector3.class);
		Vector3 rayEnd = Cache.get(Vector3.class);
		lastMove.set(endHPoint.x - startHPoint.x, endHPoint.y - startHPoint.y, ray.direction.z * camera.far);
		rayEnd.set(endHPoint.x, endHPoint.y, ray.origin.z + ray.direction.z * camera.far);
		Entity touchEntity = null;
		float maxK = 0f;
		rowLoop:
		while(i * rowStep <= endPointRow * rowStep){
			int j = startPointCol;
			boolean colIntersect = false;
			colLoop:
			while(j * colStep <= endPointCol * colStep){
				District<ModelBrickSubject, ?> district = world.getDistrict(i,  j);
				if(district != null){
					if(district.intersectLine(startHPoint, endHPoint)){
						Area districtArea = district.getBuffer();
						areaLoop:
						for(int k = 0; k < districtArea.capacity(); k++){
							Subject subject = districtArea.getSubject(k);
							if(subject == null){
								continue areaLoop;
							}
							if(subject instanceof ModelBrickSubject){
								Entity entity = subject.getEntity();
								if(InteractiveHandler.canHandle(entity)){
									if(checkedEntities.contains(entity)){
										continue areaLoop;
									}
									MBPrint mbPrint = ((ModelBrickSubject<?, ?, ?, ?, ModelBrick<? extends MBPrint>>) subject).linkModelBrick().getSafePrint();
									Arrays.fill(lenK, 0f);
									lsAlgorithm.lineCrosSkeletonAll(mbPrint, rayEnd, lastMove, lenK);
									for(int l = 0; l < lenK.length; l++){
										if(lenK[l] > maxK){
											touchEntity = entity;
											maxK = lenK[l];
										}
									}
								}
							}
						}
						if(maxK > 0){
							break rowLoop;
						}
						maxK = 0f;
						colIntersect = true;
					}else  if(colIntersect){
						break colLoop;
					}else{
						startPointCol += colStep;
					}
				}
				j += colStep;
			}
			i += rowStep;
		}
		if(maxK > 0){
			Validate.isFalse(touchEntity == null, "TouchEntity must be set");
			Interactive interactive = InteractiveHandler.getHandle(touchEntity);
			Validate.isFalse(interactive == null, "Strange Error: no Handler for " + touchEntity.getClass().getCanonicalName());
			Vector3 touchPoint = new Vector3(rayEnd.x - lastMove.x * maxK, rayEnd.y - lastMove.y * maxK, rayEnd.z - lastMove.z * maxK);
			interactive.handleTap(touchEntity, touchPoint);
		}else if(defaultHandler != null){
			defaultHandler.handleTap(ray);
		}
		Cache.put(lastMove);
		Cache.put(rayEnd);
		checkedEntities.clear();
	}
	
	public static interface DefaultHandler{
		void handleTap(Ray ray);
	}
}
