package org.bircks.entierprise.model;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public class ModelStorage implements Disposable{

	private Model model;
	private static final ModelStorage storage = new ModelStorage();
	
	
	private ModelStorage(){};
	
	public static ModelStorage instance(){
		return storage;
	}
	
	public void init(String modelConstructToolName){
		Class<? extends ModelConstructTool> tool;
		try {
			tool = (Class<? extends ModelConstructTool>) Class.forName(modelConstructToolName);
			ModelConstructTool constructTool = tool.getConstructor().newInstance();
			model = constructTool.produceModel();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	//TODO resolve somehow without synchronization
	public synchronized ModelInstance getModelInstance(String... ids){
		ModelInstance res = new ModelInstance(model, ids);
		return res;
	}
	
	public void dispose(){
		if(model != null){
			model.dispose();
		}
	}
}
