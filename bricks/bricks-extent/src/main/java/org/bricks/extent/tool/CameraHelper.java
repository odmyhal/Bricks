package org.bricks.extent.tool;

import java.util.Iterator;
import java.util.prefs.Preferences;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.item.Stone;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.Area;
import org.bricks.engine.pool.AreaBase;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Subject;
import org.bricks.exception.Validate;
import org.bricks.extent.entity.mesh.ModelSubjectSync;
import org.bricks.extent.subject.model.ContainsMBPrint;
import org.bricks.utils.HashLoop;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

/**
 * This helper should be used only in Render Thread
 * @author oleh
 *
 */
public class CameraHelper {

	private static final Dimentions cameraDimentions = new Dimentions();
	private static final Array<Fpoint> extrems = new Array<Fpoint>(false, 10);
	private static final LinkLoop<District> cameraDistricts = new LinkLoop<District>();
	private static final float worldAltituteMax = Preferences.userRoot().node("engine.settings").getFloat("world.altitude.max", 10000f);
	private static final float worldAltituteMin = Preferences.userRoot().node("engine.settings").getFloat("world.altitude.min", -100f);
	private static final float altituteHalf = (worldAltituteMax - worldAltituteMin) / 2;
	private static final float altituteCenter = (worldAltituteMax + worldAltituteMin) / 2;
	private static final float checkRadius = Preferences.userRoot().node("engine.settings").getFloat("buffer.luft", 1000f) / 2;
	private static final HashLoop<RenderableProvider> renderables = new HashLoop<RenderableProvider>();
	static{
		for(int i = 0; i < 10; i++){
			extrems.add(new Fpoint());
		}
	}
	
	private static final void applyDimentions(Camera camera){
		applyDimentions(camera.getPickRay(0f, 0f), camera.far, 0);
		applyDimentions(camera.getPickRay(0f, camera.viewportHeight), camera.far, 2);
		applyDimentions(camera.getPickRay(camera.viewportWidth, camera.viewportHeight), camera.far, 4);
		applyDimentions(camera.getPickRay(camera.viewportWidth, 0f), camera.far, 6);
		applyDimentions(camera.getPickRay(camera.viewportWidth / 2, camera.viewportHeight / 2), camera.far, 8);
	}
	
	private static final void applyDimentions(Ray ray, float far, int extremIndex){
		Fpoint start = extrems.get(extremIndex);
		Fpoint finish = extrems.get(extremIndex + 1);
		start.x = ray.origin.x;
		start.y = ray.origin.y;
		finish.x = ray.origin.x + ray.direction.x * far;
		finish.y = ray.origin.y + ray.direction.y * far;
		cameraDimentions.applyPoint(start);
		cameraDimentions.applyPoint(finish);
	}
	
	private static void setDistrictsOfDimentions(World<RenderableProvider> world){
		cameraDistricts.clear();
		int startRow = world.defineRowOfPointSectorY(cameraDimentions.getMinY());
		int finishRow = world.defineRowOfPointSectorY(cameraDimentions.getMaxY()) + 1;
		int startCol = world.defineColOfPointSectorX(cameraDimentions.getMinX());
		int finishCol = world.defineColOfPointSectorX(cameraDimentions.getMaxX()) + 1;
		for(int i = startRow; i < finishRow; i++){
			for(int j = startCol; j < finishCol; j++){
				District d = world.getDistrict(i, j);
				if(d != null){
					cameraDistricts.add(d);
				}
			}
		}
	}
	
	private static void setCameraDistricts(Camera camera){
		Iterator<District> dIterator = cameraDistricts.iterator();
		while(dIterator.hasNext()){
			District d = dIterator.next();
			Point corner = d.getCorner();
			Point habarites = d.getDimentions();
			float halfWidth = habarites.getFY() / 2;
			float halfHeight = habarites.getFY() / 2;
			float x = corner.getFX() + halfWidth;
			float y = corner.getFY() + halfHeight;
			if(camera.frustum.boundsInFrustum(x, y, altituteCenter, halfWidth, halfHeight, altituteHalf)){
				d.incrementCameraShoot();
			}else{
				dIterator.remove();
			}
		}
	}
	
	private static void setCameraRenderables(Camera camera){
		renderables.clear();
		for(District d : cameraDistricts){
			Area area = d.getBuffer();
			for(int i = 0; i < area.capacity(); i++){
				Subject subject = area.getSubject(i);
				if(subject != null){
					Imprint subjectPrint = subject.getSafePrint();
					if(subjectPrint instanceof ContainsMBPrint){
						Vector3 center = ((ContainsMBPrint) subjectPrint).linkModelBrickPrint().getCenter();
						Entity entity = subject.getEntity();
						if( (entity instanceof Stone) || camera.frustum.sphereInFrustum(center, checkRadius)){
							Validate.isTrue((entity instanceof RenderableProvider), "Wrong entity found " + entity.getClass().getCanonicalName());
							RenderableProvider rr = (RenderableProvider) entity;
							renderables.add(rr);
						}
					}//TODO: remove this:
					else if(subjectPrint instanceof EntityPointsPrint){//Need just to support deprecated ModelSubjectSync
						Point center = ((EntityPointsPrint) subjectPrint).getCenter();
						Entity entity = subject.getEntity();
						if( (entity instanceof Stone) || camera.frustum.sphereInFrustum(center.getFX(), center.getFY(), 0f, checkRadius)){
							Validate.isTrue((entity instanceof RenderableProvider), "Wrong entity found " + entity.getClass().getCanonicalName());
							RenderableProvider rr = (RenderableProvider) entity;
							renderables.add(rr);
						}
					}
					subjectPrint.free();
				}
			}
		}
	}
	
	public static final void tuneWorldCamera(World world, Camera camera){
		applyDimentions(camera);
		setDistrictsOfDimentions(world);
		setCameraDistricts(camera);
		setCameraRenderables(camera);
	}
	
	public static final Iterable<District> getInCameraDistricts(){
		return cameraDistricts;
	}
	
	public static final Iterable<RenderableProvider> getCameraRenderables(){
		return renderables;
	}
}
