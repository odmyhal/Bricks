package org.bricks.enterprise.d3.help;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bricks.enterprise.d3.help.Vector3Helper;

public class ModelConstructHelper {

	private static final Vector2 uv = new Vector2(0, 1);
	
	public static void applyRect(Vector3 v0, Vector3 v1, Vector3 v2, Vector3 v3, MeshPartBuilder meshBuilder){
		Vector3 nor = Vector3Helper.instance().calcNormal(v0, v1, v2);
		short i0 = meshBuilder.vertex(v0, nor, null, uv);
		short i1 = meshBuilder.vertex(v1, nor, null, uv);
		short i2 = meshBuilder.vertex(v2, nor, null, uv);
		short i3 = meshBuilder.vertex(v3, nor, null, uv);
		meshBuilder.index(i0, i2, i1);
		meshBuilder.index(i1, i2, i3);
	}
	
	public static void apply2TrianglesNormal(Vector3 v0, Vector3 v1, Vector3 v2, Vector3 v3, MeshPartBuilder meshBuilder){
		Vector3 nor1 = Vector3Helper.instance().calcNormal(v0, v1, v2);
		short i0 = meshBuilder.vertex(v0, nor1, null, null);
		short i1 = meshBuilder.vertex(v1, nor1, null, null);
		short i2 = meshBuilder.vertex(v2, nor1, null, null);
		meshBuilder.index(i0, i2, i1);
		
		Vector3 nor2 = Vector3Helper.instance().calcNormal(v2, v1, v3);
		i0 = meshBuilder.vertex(v2, nor2, null, null);
		i1 = meshBuilder.vertex(v3, nor2, null, null);
		i2 = meshBuilder.vertex(v1, nor2, null, null);
		
		meshBuilder.index(i0, i1, i2);
	}
	
	public static void apply2Triangles(Vector3 v0, Vector3 v1, Vector3 v2, Vector3 v3, MeshPartBuilder meshBuilder){
		short i0 = meshBuilder.vertex(v0, null, null, null);
		short i1 = meshBuilder.vertex(v1, null, null, null);
		short i2 = meshBuilder.vertex(v2, null, null, null);
		meshBuilder.index(i0, i2, i1);

		i0 = meshBuilder.vertex(v2, null, null, null);
		i1 = meshBuilder.vertex(v3, null, null, null);
		i2 = meshBuilder.vertex(v1, null, null, null);
		meshBuilder.index(i0, i1, i2);
	}
	
	public static void applyTriangleNormal(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 n, MeshPartBuilder meshBuilder){
		short i1 = meshBuilder.vertex(v1, n, null, null);
		short i2 = meshBuilder.vertex(v2, n, null, null);
		short i3 = meshBuilder.vertex(v3, n, null, null);
		meshBuilder.index(i1, i2, i3);
	}
	
	public static void applyTriangleNC(Vector3 v1, Vector3 v2, Vector3 v3, MeshPartBuilder meshBuilder){
		Vector3 nor = Vector3Helper.instance().calcNormal(v3, v2, v1);
		short i1 = meshBuilder.vertex(v1, nor, null, null);
		short i2 = meshBuilder.vertex(v2, nor, null, null);
		short i3 = meshBuilder.vertex(v3, nor, null, null);
		meshBuilder.index(i1, i2, i3);
	}
	
}
