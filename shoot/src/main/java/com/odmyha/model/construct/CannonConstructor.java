package com.odmyha.model.construct;

import java.util.ArrayList;
import java.util.List;

import org.bircks.entierprise.model.ModelConstructTool;
import org.bircks.entierprise.model.ModelConstructor;
import org.bricks.annotation.ConstructModel;
import org.bricks.enterprise.d3.help.ModelConstructHelper;
import org.bricks.extent.rewrite.Matrix4Safe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

@ConstructModel({"tower", "tube"})
public class CannonConstructor implements ModelConstructor{

	private static final CannonConstructor instance = new CannonConstructor();
	
	private CannonConstructor(){}
	
	public static ModelConstructor instance(){
		return instance;
	}
	
	public void construct(ModelConstructTool modelBuilder, String... partName) {
		produceTowerModel(modelBuilder, partName[0]);
		produceTubeModel(modelBuilder, partName[1]);
	}
	
	private void produceTowerModel(ModelConstructTool modelBuilder, String towerName){
		
		modelBuilder.node(towerName);
		
		List<Vector3> vertexes = new ArrayList<Vector3>();
		vertexes.add(new Vector3(150f, 175f, -35f));
		vertexes.add(new Vector3(150f, 175f, 25f));
		vertexes.add(new Vector3(125f, 200f, -35f));
		vertexes.add(new Vector3(125f, 200f, 35f));
		vertexes.add(new Vector3(25f, 200f, -35f));
		vertexes.add(new Vector3(25f, 200f, 35f));
		vertexes.add(new Vector3(0f, 175f, -35f));
		vertexes.add(new Vector3(0f, 175f, 25f));
		vertexes.add(new Vector3(0f, 100f, -35f));
		vertexes.add(new Vector3(0f, 100f, 25f));
		vertexes.add(new Vector3(50f, 50f, -35f));
		vertexes.add(new Vector3(50f, 50f, 30f));
		vertexes.add(new Vector3(100f, 50f, -35f));
		vertexes.add(new Vector3(100f, 50f, 30f));
		vertexes.add(new Vector3(150f, 100f, -35f));
		vertexes.add(new Vector3(150f, 100f, 25f));
	
		MeshPartBuilder meshBuilder = modelBuilder.part("tower", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));

		Matrix4Safe m4 = new Matrix4Safe();
//		m4.translate(-75f, -125f, 0f);
//		m4.trn(-75f, -125f, 0f);
		m4.setToRotation(0f, 0f, 10f, -90f);
		m4.trn(-125f, 75f, 0f);
		meshBuilder.setVertexTransform(m4);
		
		for(int i = 0; i < vertexes.size(); i += 2){
			int two = (i + 2) % vertexes.size();
			int thr = (i + 3) % vertexes.size();
			ModelConstructHelper.applyRect(vertexes.get(i), vertexes.get(i + 1), vertexes.get(two), vertexes.get(thr), meshBuilder);
		}
		
		ModelConstructHelper.applyRect(vertexes.get(13), vertexes.get(3), vertexes.get(15), vertexes.get(1), meshBuilder);
		ModelConstructHelper.applyRect(vertexes.get(11), vertexes.get(5), vertexes.get(13), vertexes.get(3), meshBuilder);
		ModelConstructHelper.applyRect(vertexes.get(9), vertexes.get(7), vertexes.get(11), vertexes.get(5), meshBuilder);

	}
	
	

	private void produceTubeModel(ModelConstructTool modelBuilder, String towerName){
		
		modelBuilder.node(towerName);
		
		MeshPartBuilder meshBuilder = modelBuilder.part(towerName, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
		Matrix4Safe m4 = new Matrix4Safe();
		m4.setToRotation(0f, 0f, 10f, -90f);
		m4.trn(125f, 0f, 0f);
		meshBuilder.setVertexTransform(m4);
		meshBuilder.cylinder(50, 100, 50, 1000);
	}
}
