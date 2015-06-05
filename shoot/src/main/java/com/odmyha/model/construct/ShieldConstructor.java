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

@ConstructModel("shield")
public class ShieldConstructor implements ModelConstructor{
	
	private static final ShieldConstructor instance = new ShieldConstructor();
	
	private ShieldConstructor(){}
	
	public static ModelConstructor instance(){
		return instance;
	}

	public void construct(ModelConstructTool modelBuilder, String... partName) {
		
		modelBuilder.node(partName[0]);
		
		MeshPartBuilder meshBuilder = modelBuilder.part(partName[0], GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)));
		Matrix4Safe tm = new Matrix4Safe();
//		tm.trn(0f, 0f, -30f);
		meshBuilder.setVertexTransform(tm);
		meshBuilder.box(50, 200, 60);
//		meshBuilder.box(60, 60, 60);
	}

}
