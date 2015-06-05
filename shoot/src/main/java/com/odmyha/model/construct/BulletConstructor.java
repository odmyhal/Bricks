package com.odmyha.model.construct;

import org.bircks.entierprise.model.ModelConstructTool;
import org.bircks.entierprise.model.ModelConstructor;
import org.bricks.annotation.ConstructModel;
import org.bricks.extent.rewrite.Matrix4Safe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix4;

@ConstructModel({"bullet"})
public class BulletConstructor implements ModelConstructor{
	
	private static final BulletConstructor instance = new BulletConstructor();
	
	private BulletConstructor(){}
	
	public static ModelConstructor instance(){
		return instance;
	}

	public void construct(ModelConstructTool modelBuilder, String... partName) {
		modelBuilder.node(partName[0]);
		MeshPartBuilder meshBuilder = modelBuilder.part("gilse", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.ORANGE)));
		Matrix4Safe tmGls = new Matrix4Safe();
		tmGls.setToRotation(0f, 0f, 10f, -90f);
		meshBuilder.setVertexTransform(tmGls);
		meshBuilder.cylinder(25f, 27f, 25f, 100);
		
		meshBuilder = modelBuilder.part("cone", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.ORANGE)));
	    Matrix4Safe tmCone = new Matrix4Safe();
	    tmCone.setToRotation(0f, 0f, 10f, -90f);
		tmCone.trn(20f, 0f, 0f);;
		meshBuilder.setVertexTransform(tmCone);
		meshBuilder.cone(25, 13, 25, 100);
		
//		meshBuilder.cone(30, 13, 30, 100);
	}

}
