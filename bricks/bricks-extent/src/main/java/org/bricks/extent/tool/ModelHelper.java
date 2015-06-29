package org.bricks.extent.tool;

import org.bricks.engine.item.Stone;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.rewrite.Matrix4Safe;
import org.bricks.extent.space.Roll3D;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class ModelHelper {
	

	private static final ThreadLocal<Roll> tmpRoll = new ThreadLocal<Roll>(){
		@Override protected Roll initialValue() {
            return new Roll3D();
        }
	};

	public static void calculateNodeGlobalTransforms(Node node){
		node.calculateWorldTransform();
		for(Node chn : node.getChildren()){
			calculateNodeGlobalTransforms(chn);
		}
		//use with node 1.3.1 version
/*		for(Node chn : node.children){
			calculateNodeGlobalTransforms(chn);
		}*/
	}
	
	public static Node findNode(String nodePath, Iterable<Node> nodes){
		int splitIndex = nodePath.indexOf('/');
		String curName;
		if(splitIndex < 0){
			curName = nodePath;
		}else{
			curName = nodePath.substring(0, splitIndex);
		}
		for(Node node : nodes){
			if(curName.equals(node.id)){
				if(curName.equals(nodePath)){
					return node;
				}else{
					return findNode(nodePath.substring(splitIndex + 1, nodePath.length()), node.getChildren());
				}
			}
		}
		return null;
	}
	
	/**
	 * Method used for Model nodes, which can be iterable in multiple threads
	 * @param nodePath
	 * @param nodes
	 * @return
	 */
	public static Node findSafeNode(String nodePath, Array<Node> nodes){
		int splitIndex = nodePath.indexOf('/');
		String curName;
		if(splitIndex < 0){
			curName = nodePath;
		}else{
			curName = nodePath.substring(0, splitIndex);
		}
		for(int i = 0; i < nodes.size; i++){
			Node node = nodes.get(i);
			if(curName.equals(node.id)){
				if(curName.equals(nodePath)){
					return node;
				}else{
					return findSafeNode(nodePath.substring(splitIndex + 1, nodePath.length()), (Array<Node>) node.getChildren());
				}
			}
		}
		return null;
	}
	
	public static final void mmultLeft(Matrix4 m, Matrix4 target){
		float[] tmp = Matrix4Safe.safeTmpArray();
		mmult(m, target, tmp);
		target.set(tmp);
	}
	
	public static final void mmultRight(Matrix4 target, Matrix4 m){
		float[] tmp = Matrix4Safe.safeTmpArray();
		mmult(m, target, tmp);
		target.set(tmp);
	}
	
	private static final void mmult(Matrix4 one, Matrix4 two, float tmp[]){
		arrayMmult(one.val, two.val, tmp);
	}
	
	private static final void mmultDouble(Matrix4 one, Matrix4 two, float tmp[]){
		arrayMmult(one.val, two.val, tmp);
	}
	
	public static double[] castToDouble(float[] src){
		double[] dest = new double[src.length];
		for(int i = 0; i < src.length; i++){
			dest[i] = (double) src[i];
		}
		return dest;
	}
	
	public static float[] castToFloat(double[] src){
		float[] dest = new float[src.length];
		for(int i = 0; i < src.length; i++){
			dest[i] = (float) src[i];
		}
		return dest;
	}
	
	private static final void arrayMmult(float[] one, float[] two, float[] tmp){
		tmp[Matrix4.M00] = one[Matrix4.M00] * two[Matrix4.M00] + one[Matrix4.M01] * two[Matrix4.M10] + one[Matrix4.M02] * two[Matrix4.M20] + one[Matrix4.M03] * two[Matrix4.M30];
		tmp[Matrix4.M01] = one[Matrix4.M00] * two[Matrix4.M01] + one[Matrix4.M01] * two[Matrix4.M11] + one[Matrix4.M02] * two[Matrix4.M21] + one[Matrix4.M03] * two[Matrix4.M31];
		tmp[Matrix4.M02] = one[Matrix4.M00] * two[Matrix4.M02] + one[Matrix4.M01] * two[Matrix4.M12] + one[Matrix4.M02] * two[Matrix4.M22] + one[Matrix4.M03] * two[Matrix4.M32];
		tmp[Matrix4.M03] = one[Matrix4.M00] * two[Matrix4.M03] + one[Matrix4.M01] * two[Matrix4.M13] + one[Matrix4.M02] * two[Matrix4.M23] + one[Matrix4.M03] * two[Matrix4.M33];
		
		tmp[Matrix4.M10] = one[Matrix4.M10] * two[Matrix4.M00] + one[Matrix4.M11] * two[Matrix4.M10] + one[Matrix4.M12] * two[Matrix4.M20] + one[Matrix4.M13] * two[Matrix4.M30];
		tmp[Matrix4.M11] = one[Matrix4.M10] * two[Matrix4.M01] + one[Matrix4.M11] * two[Matrix4.M11] + one[Matrix4.M12] * two[Matrix4.M21] + one[Matrix4.M13] * two[Matrix4.M31];
		tmp[Matrix4.M12] = one[Matrix4.M10] * two[Matrix4.M02] + one[Matrix4.M11] * two[Matrix4.M12] + one[Matrix4.M12] * two[Matrix4.M22] + one[Matrix4.M13] * two[Matrix4.M32];
		tmp[Matrix4.M13] = one[Matrix4.M10] * two[Matrix4.M03] + one[Matrix4.M11] * two[Matrix4.M13] + one[Matrix4.M12] * two[Matrix4.M23] + one[Matrix4.M13] * two[Matrix4.M33];
		
		tmp[Matrix4.M20] = one[Matrix4.M20] * two[Matrix4.M00] + one[Matrix4.M21] * two[Matrix4.M10] + one[Matrix4.M22] * two[Matrix4.M20] + one[Matrix4.M23] * two[Matrix4.M30];
		tmp[Matrix4.M21] = one[Matrix4.M20] * two[Matrix4.M01] + one[Matrix4.M21] * two[Matrix4.M11] + one[Matrix4.M22] * two[Matrix4.M21] + one[Matrix4.M23] * two[Matrix4.M31];
		tmp[Matrix4.M22] = one[Matrix4.M20] * two[Matrix4.M02] + one[Matrix4.M21] * two[Matrix4.M12] + one[Matrix4.M22] * two[Matrix4.M22] + one[Matrix4.M23] * two[Matrix4.M32];
		tmp[Matrix4.M23] = one[Matrix4.M20] * two[Matrix4.M03] + one[Matrix4.M21] * two[Matrix4.M13] + one[Matrix4.M22] * two[Matrix4.M23] + one[Matrix4.M23] * two[Matrix4.M33];
		
		tmp[Matrix4.M30] = one[Matrix4.M30] * two[Matrix4.M00] + one[Matrix4.M31] * two[Matrix4.M10] + one[Matrix4.M32] * two[Matrix4.M20] + one[Matrix4.M33] * two[Matrix4.M30];
		tmp[Matrix4.M31] = one[Matrix4.M30] * two[Matrix4.M01] + one[Matrix4.M31] * two[Matrix4.M11] + one[Matrix4.M32] * two[Matrix4.M21] + one[Matrix4.M33] * two[Matrix4.M31];
		tmp[Matrix4.M32] = one[Matrix4.M30] * two[Matrix4.M02] + one[Matrix4.M31] * two[Matrix4.M12] + one[Matrix4.M32] * two[Matrix4.M22] + one[Matrix4.M33] * two[Matrix4.M32];
		tmp[Matrix4.M33] = one[Matrix4.M30] * two[Matrix4.M03] + one[Matrix4.M31] * two[Matrix4.M13] + one[Matrix4.M32] * two[Matrix4.M23] + one[Matrix4.M33] * two[Matrix4.M33];
	}
	
	public static final void arrayMmultDouble(double[] one, double[] two, double[] tmp){
		tmp[Matrix4.M00] = one[Matrix4.M00] * two[Matrix4.M00] + one[Matrix4.M01] * two[Matrix4.M10] + one[Matrix4.M02] * two[Matrix4.M20] + one[Matrix4.M03] * two[Matrix4.M30];
		tmp[Matrix4.M01] = one[Matrix4.M00] * two[Matrix4.M01] + one[Matrix4.M01] * two[Matrix4.M11] + one[Matrix4.M02] * two[Matrix4.M21] + one[Matrix4.M03] * two[Matrix4.M31];
		tmp[Matrix4.M02] = one[Matrix4.M00] * two[Matrix4.M02] + one[Matrix4.M01] * two[Matrix4.M12] + one[Matrix4.M02] * two[Matrix4.M22] + one[Matrix4.M03] * two[Matrix4.M32];
		tmp[Matrix4.M03] = one[Matrix4.M00] * two[Matrix4.M03] + one[Matrix4.M01] * two[Matrix4.M13] + one[Matrix4.M02] * two[Matrix4.M23] + one[Matrix4.M03] * two[Matrix4.M33];
		
		tmp[Matrix4.M10] = one[Matrix4.M10] * two[Matrix4.M00] + one[Matrix4.M11] * two[Matrix4.M10] + one[Matrix4.M12] * two[Matrix4.M20] + one[Matrix4.M13] * two[Matrix4.M30];
		tmp[Matrix4.M11] = one[Matrix4.M10] * two[Matrix4.M01] + one[Matrix4.M11] * two[Matrix4.M11] + one[Matrix4.M12] * two[Matrix4.M21] + one[Matrix4.M13] * two[Matrix4.M31];
		tmp[Matrix4.M12] = one[Matrix4.M10] * two[Matrix4.M02] + one[Matrix4.M11] * two[Matrix4.M12] + one[Matrix4.M12] * two[Matrix4.M22] + one[Matrix4.M13] * two[Matrix4.M32];
		tmp[Matrix4.M13] = one[Matrix4.M10] * two[Matrix4.M03] + one[Matrix4.M11] * two[Matrix4.M13] + one[Matrix4.M12] * two[Matrix4.M23] + one[Matrix4.M13] * two[Matrix4.M33];
		
		tmp[Matrix4.M20] = one[Matrix4.M20] * two[Matrix4.M00] + one[Matrix4.M21] * two[Matrix4.M10] + one[Matrix4.M22] * two[Matrix4.M20] + one[Matrix4.M23] * two[Matrix4.M30];
		tmp[Matrix4.M21] = one[Matrix4.M20] * two[Matrix4.M01] + one[Matrix4.M21] * two[Matrix4.M11] + one[Matrix4.M22] * two[Matrix4.M21] + one[Matrix4.M23] * two[Matrix4.M31];
		tmp[Matrix4.M22] = one[Matrix4.M20] * two[Matrix4.M02] + one[Matrix4.M21] * two[Matrix4.M12] + one[Matrix4.M22] * two[Matrix4.M22] + one[Matrix4.M23] * two[Matrix4.M32];
		tmp[Matrix4.M23] = one[Matrix4.M20] * two[Matrix4.M03] + one[Matrix4.M21] * two[Matrix4.M13] + one[Matrix4.M22] * two[Matrix4.M23] + one[Matrix4.M23] * two[Matrix4.M33];
		
		tmp[Matrix4.M30] = one[Matrix4.M30] * two[Matrix4.M00] + one[Matrix4.M31] * two[Matrix4.M10] + one[Matrix4.M32] * two[Matrix4.M20] + one[Matrix4.M33] * two[Matrix4.M30];
		tmp[Matrix4.M31] = one[Matrix4.M30] * two[Matrix4.M01] + one[Matrix4.M31] * two[Matrix4.M11] + one[Matrix4.M32] * two[Matrix4.M21] + one[Matrix4.M33] * two[Matrix4.M31];
		tmp[Matrix4.M32] = one[Matrix4.M30] * two[Matrix4.M02] + one[Matrix4.M31] * two[Matrix4.M12] + one[Matrix4.M32] * two[Matrix4.M22] + one[Matrix4.M33] * two[Matrix4.M32];
		tmp[Matrix4.M33] = one[Matrix4.M30] * two[Matrix4.M03] + one[Matrix4.M31] * two[Matrix4.M13] + one[Matrix4.M32] * two[Matrix4.M23] + one[Matrix4.M33] * two[Matrix4.M33];
	}
	
	public static void doubleMatrixTra(double[] src, double[] dest){
		dest[Matrix4.M00] = src[Matrix4.M00];
		dest[Matrix4.M01] = src[Matrix4.M10];
		dest[Matrix4.M02] = src[Matrix4.M20];
		dest[Matrix4.M03] = src[Matrix4.M30];
		dest[Matrix4.M10] = src[Matrix4.M01];
		dest[Matrix4.M11] = src[Matrix4.M11];
		dest[Matrix4.M12] = src[Matrix4.M21];
		dest[Matrix4.M13] = src[Matrix4.M31];
		dest[Matrix4.M20] = src[Matrix4.M02];
		dest[Matrix4.M21] = src[Matrix4.M12];
		dest[Matrix4.M22] = src[Matrix4.M22];
		dest[Matrix4.M23] = src[Matrix4.M32];
		dest[Matrix4.M30] = src[Matrix4.M03];
		dest[Matrix4.M31] = src[Matrix4.M13];
		dest[Matrix4.M32] = src[Matrix4.M23];
		dest[Matrix4.M33] = src[Matrix4.M33];
	}

	public static void setToRotation(float rad, Stone<?, ?, ?> target){
		Roll roll = tmpRoll.get();
		//Important previously to set zero for modeled stones
		roll.setRotation(0);
		roll.setRotation(rad);
		for(int i = 0; i < target.getSatellites().size(); i++){
			target.getSatellites().get(i).rotate(roll, target.origin());
		}
		target.adjustCurrentPrint();
	}
	
}
