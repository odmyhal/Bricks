package org.bricks.extent.tool;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bircks.entierprise.model.ModelConstructTool;
import org.bircks.entierprise.model.ModelConstructor;
import org.bricks.core.entity.Tuple;
import org.bricks.enterprise.d3.help.ModelConstructHelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector3;

public abstract class SkeletonConstructor implements ModelConstructor{

	protected void applyRect(MeshPartBuilder meshBuilder, List<Vector3> vertexes, Collection<Integer> indexData, 
			int i1, int i2, int i3, int i4){
		ModelConstructHelper.applyRect(vertexes.get(i1), vertexes.get(i2), vertexes.get(i3), vertexes.get(i4), meshBuilder);
		indexData.add(i1);
		indexData.add(i2);
		indexData.add(i3);
		
		indexData.add(i2);
		indexData.add(i3);
		indexData.add(i4);
	}
	
	protected void applyTriangleNC(MeshPartBuilder meshBuilder, List<Vector3> vertexes, Collection<Integer> indexData,
			int i1, int i2, int i3){
		ModelConstructHelper.applyTriangleNC(vertexes.get(i1), vertexes.get(i2), vertexes.get(i3), meshBuilder);
		indexData.add(i1);
		indexData.add(i2);
		indexData.add(i3);
	}
	
	protected void constructDebug(ModelConstructTool modelBuilder, String debugName, Vector3[] vertexData, int[] indexData){
		modelBuilder.node(debugName);
		MeshPartBuilder meshBuilder = modelBuilder.part("debug", GL20.GL_LINES, Usage.Position, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
		
		Set<Tuple> tuples = new HashSet();
		for(int i = 0; i < indexData.length; i += 3){
			for(int j = 0; j<3; j++){
				Vector3 A = vertexData[indexData[i + j]];
				Vector3 B = vertexData[indexData[i + (j + 1) % 3]];
				Tuple<Vector3, Vector3> ab = new Tuple<Vector3, Vector3>(A, B);
				if(!tuples.contains(ab)){
					meshBuilder.line(A, B);
					tuples.add(ab);
					tuples.add(new Tuple(B, A));
				}
			}
			
		}
	}
}
