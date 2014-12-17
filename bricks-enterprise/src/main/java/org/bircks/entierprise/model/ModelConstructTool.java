package org.bircks.entierprise.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public abstract class ModelConstructTool  extends ModelBuilder{
	
	public Model produceModel(){
		this.begin();
		constructModels();
		return this.end();
	}
	
	protected abstract void constructModels();
	
	public void node(String name){
		Node node = super.node();
		node.id = name;
	}

}
