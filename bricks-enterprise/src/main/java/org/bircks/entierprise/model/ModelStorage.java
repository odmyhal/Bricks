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
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ModelInstance getModelInstance(String... ids){
		ModelInstance res = new ModelInstance(model, ids);
		return res;
	}
	
	public void dispose(){
		if(model != null){
			model.dispose();
		}
	}
}
