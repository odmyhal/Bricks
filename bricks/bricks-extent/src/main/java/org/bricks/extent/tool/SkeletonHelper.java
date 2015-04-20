package org.bricks.extent.tool;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bricks.core.entity.Tuple;
import org.bricks.exception.Validate;
import org.bricks.extent.space.overlap.Skeleton;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Vector3;

public class SkeletonHelper {

	public static Skeleton fetchSkeletonFromModel(ModelInstance modelInstance, String nodePath){
		Tuple tpl = fetchSkeletonDataFromModel(modelInstance, nodePath);
		int[] intData = (int[]) tpl.getFirst();
		Vector3[] vertexData = (Vector3[]) tpl.getSecond();
		return new Skeleton(intData, vertexData);
	}
	
	public static Tuple fetchSkeletonDataFromModel(ModelInstance modelInstance, String nodePath){
		List<Integer> indexes = new ArrayList<Integer>();
		List<Vector3> vertexes = new ArrayList<Vector3>();
		
		Node node = ModelHelper.findNode(nodePath, modelInstance.nodes);
		System.out.println("Skeleton helper checking " + node.parts.size + " of node " + nodePath);
		for(NodePart nodePart : node.parts){
//			System.out.println("Skeleton helper checks node " + nodePart.)
			MeshPart meshPart = nodePart.meshPart;
			Mesh mesh = meshPart.mesh;
			if(mesh.getNumIndices() <= 0){
				continue;
			}
/*			if(mesh.getNumIndices() > 0){
				System.out.println("Mesh is indexed");
			}else{
				System.out.println("Mesh is not indexed");
				continue;
			}*/
			FloatBuffer fb = mesh.getVerticesBuffer();
			ShortBuffer sb = mesh.getIndicesBuffer();
			
			short[] meshIndices = new short[meshPart.numVertices];
			sb.position(meshPart.indexOffset);
//			sb.limit(meshPart.numVertices);
			sb.get(meshIndices);
//			sb.get(meshIndices, meshPart.indexOffset, meshPart.numVertices);
//			mesh.getIndices(meshIndices, meshPart.indexOffset);
			
			int step = calcStepLen(mesh.getVertexAttributes());
			Map<Short, Integer> cacheIndex = new HashMap<Short, Integer>();
			for(short ind : meshIndices){
				if(cacheIndex.containsKey(ind)){
					indexes.add(cacheIndex.get(ind));
				}else{
					int find = ind * step;
					float x = fb.get(find);
					float y = fb.get(find + 1);
					float z = fb.get(find + 2);
					Vector3 vertex = new Vector3(x, y, z);
//					vertex.mul(node.globalTransform);
					vertexes.add(vertex);
					int myInd = vertexes.size() - 1;
					indexes.add(myInd);
					cacheIndex.put(ind, myInd);
				}
			}
		}
		Validate.isTrue(vertexes.size() > 0, "It has indexes to exist");
		int[] intData = new int[indexes.size()];
		for(int i = 0; i < intData.length; i++){
			intData[i] = indexes.get(i);
		}
		return new Tuple(intData, vertexes.toArray(new Vector3[vertexes.size()]));
	}
	
	private static final int calcStepLen(VertexAttributes va){
		int step = 0;
		long mask = va.getMask();
		if((mask & Usage.Position) == Usage.Position){
			step += 3;
		}
		if((mask & Usage.Color) == Usage.Color){
			step += 4;
		}
		if((mask & Usage.ColorPacked) == Usage.ColorPacked){
			step += 4;
		}
		if((mask & Usage.Normal) == Usage.Normal){
			step += 3;
		}
		if((mask & Usage.TextureCoordinates) == Usage.TextureCoordinates){
			step += 2;
		}
		if((mask & Usage.BoneWeight) == Usage.BoneWeight){
			step += 2;
		}
		if((mask & Usage.Tangent) == Usage.Tangent){
			step += 3;
		}
		if((mask & Usage.BiNormal) == Usage.BiNormal){
			step += 3;
		}
		return step;
	}
}
