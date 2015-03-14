package com.odmyhal.ball;

import java.io.IOException;
import java.util.Collection;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import org.bircks.enterprise.control.panel.InvisiblePanel;
import org.bircks.enterprise.control.panel.PanelManager;
import org.bircks.enterprise.control.panel.camera.CameraPanel;
import org.bircks.entierprise.model.ModelStorage;
import org.bricks.engine.Engine;
import org.bricks.extent.debug.ShapeDebugger;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.odmyha.weapon.Cannon;
import com.odmyhal.panel.CannonPanel;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

public class BallGame implements ApplicationListener {
	private Camera camera;
//	private Camera camera2d;
	private PanelManager panelManager;
	
	private ModelBatch modelBatch;
	private Environment environment;
	private final int sLen = 250;
	private Vector3 cameraMove = new Vector3(0f, 0f, 0f);
	
	private Engine<RenderableProvider> engine;

	ShapeDebugger debug;
	boolean debugEnabled = false;
	
	private int initWidth, initHeight;
	
	public BallGame(int width, int height){
		initWidth = width;
		initHeight = height;
	}
	
	@Override
	public void create() {
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
/*		
		camera2d = new OrthographicCamera(1250f, 750f);
		camera2d.translate(625f, 375f, 0);
		shR = new ShapeRenderer();
		shR.translate(-625, -375, 0);
		shR.setProjectionMatrix(camera2d.combined);
*/		
		
		camera = new PerspectiveCamera(37f, 1250f, 750f);
		camera.far = 300000;
		camera.translate(875, -600, 1200);
		camera.lookAt(875, 600, 0);	
/*		camera.translate(875, 1200, 1200);
		camera.lookAt(875, 0, 0);*/
		camera.update();

		panelManager = new PanelManager();
		CameraPanel tp = new CameraPanel(camera);
//		tp.activate();
		panelManager.addPanel(tp);
		panelManager.addPanel(new InvisiblePanel());
		
		modelBatch = new ModelBatch();
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        debug = new ShapeDebugger();

		BallProducer bp = new BallProducer();
		

//        Map<String, String> props = bp.produceProperties();
        
		engine = new Engine<RenderableProvider>();
		FileHandle fh = Gdx.files.internal("config/engine.prefs.xml");
		try {
			Preferences.importPreferences(fh.read());
		} catch (IOException e) {
			Gdx.app.error("ERROR", "Could not read file " + fh.path(), e);
		} catch (InvalidPreferencesFormatException e) {
			Gdx.app.error("ERROR", "Could not parse file " + fh.path(), e);
		}
		Preferences prefs = Preferences.userRoot().node("engine.settings");
		engine.init(prefs);
		

        ModelStorage.instance().init(prefs.get("model.construct.tool", null));
		Gdx.app.debug("OLEH-CHECK", "Before push !!!!!!!!!!!!!!!!!!!");
		
		bp.produceBall(200, 200, -50f, -50f).applyEngine(engine);
		bp.produceSmallBall(350, 300, -150f, -150f).applyEngine(engine);
		
		bp.produceSmallBall(125, 75, 205f, -210f).applyEngine(engine);
		bp.produceBall(375, 75).applyEngine(engine);
		bp.produceBall(1475, 75).applyEngine(engine);
		bp.produceSmallBall(125, 300, 135f, -210f).applyEngine(engine);
		bp.produceSmallBall(375, 300, 135f, -210f).applyEngine(engine);
		bp.produceBall(625, 333).applyEngine(engine);
		bp.produceBall(85, 300).applyEngine(engine);
		bp.produceBall(625, 625).applyEngine(engine);
		bp.produceBall(875, 625).applyEngine(engine);
		bp.produceSmallBall(225, 225, 175f, -210f).applyEngine(engine);
		bp.produceSmallBall(375, 225, 135f, -310f).applyEngine(engine);
		bp.produceBall(175, 225).applyEngine(engine);
		bp.produceSmallBall(125, 475, 235f, -210f).applyEngine(engine);
		bp.produceBall(375, 475).applyEngine(engine);
		bp.produceBall(625, 475).applyEngine(engine);
		bp.produceSmallBall(875, 475, 235f, -210f).applyEngine(engine);
		bp.produceSmallBall(500, 700, 0f, -2000f).applyEngine(engine);
		bp.produceSmallBall(1000, 400, -100f, 0f).applyEngine(engine);
		
		bp.produceSmallBall(125, 675, 1000f, -1000f).applyEngine(engine);
		

		int cnt = 0;
		for(int i=750; i < 250 * 40 - 40; i+=225){
			for(int j=50; j< 250 * 40 - 40; j+= 225){
				bp.produceBall(j, i, 400f * (i%10 == 0 ? 1 : -1), 400f * (j%10 == 0 ? -1 : 1)).applyEngine(engine);
//				bp.produceBall(j, i, 20f * (i%10 == 0 ? 1 : -1), 15f * (j%10 == 0 ? -1 : 1)).applyEngine(engine);
				cnt++;
			}
		}
		Gdx.app.debug("OLEH-CHECK", "Created " + cnt + " balls inside loop");
		
		Cannon cann = bp.produceCannon(925, 125);
		cann.setToRotation((float)(Math.PI / 2));
//		cann = bp.produceCannon(925, 925);
		cann.applyEngine(engine);
		
		CannonPanel cannonPanel = new CannonPanel(cann);
		cannonPanel.setActive(true);
		cannonPanel.inputControl();
		panelManager.addPanel(cannonPanel);
		
		bp.produceShield(1075, 100).applyEngine(engine);
		bp.produceShield(775, 100).applyEngine(engine);
		
		
//		Gdx.input.setInputProcessor(new BallAdapter(cann, cameraMove));
		
		engine.start();
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gdx.app.debug("OLEH-CHECK " + System.currentTimeMillis(), "Game started !!!!!!!!!!!!!!!!!!!");
	}
	@Override
	public void dispose() {
		ModelStorage.instance().dispose();
//		shR.dispose();
		modelBatch.dispose();
		engine.stop();
		debug.dispose();
//		Gdx.app.debug("OLEH-TEST", "Views created: " + Subject.createdView.get());
		Gdx.app.debug("OLEH-CHECK", "GAme disposed");
	}

	@Override
	public void render() {
		
		if(cameraMove.x != 0 || cameraMove.y != 0 || cameraMove.z != 0){
			float dTime = Gdx.graphics.getDeltaTime();
			camera.translate(dTime * cameraMove.x,  dTime * cameraMove.y, dTime * cameraMove.z);
			camera.update();
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//		drawSectors();
//		DataPool<RenderableProvider> entitiesPool = engine.getWorld().getRenderEntities();
		
		Collection<RenderableProvider> entities = engine.getWorld().getRenderEntities();

		modelBatch.begin(camera);
		for(RenderableProvider entity : entities){
			modelBatch.render(entity, environment);
		}

		modelBatch.end();
		if(debugEnabled){
			debug.drawEntityShapes(entities, camera.combined);
		}
//		entitiesPool.free();

		panelManager.render(Gdx.graphics.getDeltaTime());
//		renderControl();
	}
/*	
	private void renderControl(){
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		shR.line(50,  50, 1200, 50);
		shR.line(1200, 50, 1200, 700);
		shR.line(1200, 700, 50, 700);
		shR.line(50, 700, 50, 50);
		shR.end();
	}
*/
	/*
	private void drawDistrict(District d){
		if(d == null){
			Gdx.app.debug("OLEH-CHECK", "District is null");
		}
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		Point corner = d.getCorner();
		shR.line(corner.getFX(), corner.getFY(), corner.getFX() + sLen, corner.getFY());
		shR.line(corner.getFX(), corner.getFY(), corner.getFX(), corner.getFY() + sLen);
		shR.line(corner.getFX() + sLen, corner.getFY() + sLen, corner.getFX() + sLen, corner.getFY());
		shR.line(corner.getFX() + sLen, corner.getFY() + sLen, corner.getFX(), corner.getFY() + sLen);
		shR.end();
	}
*/	
	
	

	@Override
	public void resize(int width, int height) {
//		System.out.println("Resizeing " + width + " : " + height);
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
		panelManager.resizeViewport(width, height);
//		System.out.println("3d camera matrix: \n" + camera.combined);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
