package com.odmyha.model.construct;

import org.bircks.entierprise.model.ModelConstructTool;
import org.bircks.entierprise.model.ModelConstructor;
import org.bricks.annotation.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;

@ConstructModel({"ball_60", "ball_30"})
public class BallConstructor implements ModelConstructor{
	
	private static final BallConstructor instance = new BallConstructor();
	String[] s = new String[]{"ball_60", "ball_30"};
	
	private BallConstructor(){}
	
	public static ModelConstructor instance(){
		return instance;
	}

	public void construct(ModelConstructTool modelBuilder, String... partName) {
		for(String pName: partName){
			modelBuilder.node(pName);
			produceBall(modelBuilder, pName);
		}
	}

	private void produceBall(ModelBuilder modelBuilder, String name){
		String[] pName = name.split("_");
		Color ballColor = new Color(0.5f, 0.5f, 1f, 1f);
		MeshPartBuilder meshBuilder = modelBuilder.part(name, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(ballColor)));
		float d = Float.parseFloat(pName[1]);
//		Matrix4 tm = new Matrix4();
//		float df = d / 2;
//		tm.trn(df, df, df);
//		meshBuilder.setVertexTransform(tm);
		meshBuilder.setVertexTransform(new Matrix4());
		meshBuilder.sphere(d, d, d, 20, 20);
	}
	
}
