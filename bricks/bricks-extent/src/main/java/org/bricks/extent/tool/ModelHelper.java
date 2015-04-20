package org.bricks.extent.tool;

import org.bricks.engine.item.Stone;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.tool.Roll;
import org.bricks.extent.space.Roll3D;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;

public class ModelHelper {
	

	private static final ThreadLocal<Roll> tmpRoll = new ThreadLocal<Roll>(){
		@Override protected Roll initialValue() {
            return new Roll3D();
        }
	};

	public static void calculateNodeGlobalTransforms(Node node){
		node.calculateWorldTransform();
		for(Node chn : node.children){
			calculateNodeGlobalTransforms(chn);
		}
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
					return findNode(nodePath.substring(splitIndex + 1, nodePath.length()), node.children);
				}
			}
		}
		return null;
	}
	
	public static final void mmultLeft(Matrix4 m, Matrix4 target){
		mmult(m, target, target.tmp);
		target.set(target.tmp);
	}
	
	public static final void mmultRight(Matrix4 target, Matrix4 m){
		mmult(m, target, target.tmp);
		target.set(target.tmp);
	}
	
	private static final void mmult(Matrix4 one, Matrix4 two, float tmp[]){
		tmp[Matrix4.M00] = one.val[Matrix4.M00] * two.val[Matrix4.M00] + one.val[Matrix4.M01] * two.val[Matrix4.M10] + one.val[Matrix4.M02] * two.val[Matrix4.M20] + one.val[Matrix4.M03] * two.val[Matrix4.M30];
		tmp[Matrix4.M01] = one.val[Matrix4.M00] * two.val[Matrix4.M01] + one.val[Matrix4.M01] * two.val[Matrix4.M11] + one.val[Matrix4.M02] * two.val[Matrix4.M21] + one.val[Matrix4.M03] * two.val[Matrix4.M31];
		tmp[Matrix4.M02] = one.val[Matrix4.M00] * two.val[Matrix4.M02] + one.val[Matrix4.M01] * two.val[Matrix4.M12] + one.val[Matrix4.M02] * two.val[Matrix4.M22] + one.val[Matrix4.M03] * two.val[Matrix4.M32];
		tmp[Matrix4.M03] = one.val[Matrix4.M00] * two.val[Matrix4.M03] + one.val[Matrix4.M01] * two.val[Matrix4.M13] + one.val[Matrix4.M02] * two.val[Matrix4.M23] + one.val[Matrix4.M03] * two.val[Matrix4.M33];
		
		tmp[Matrix4.M10] = one.val[Matrix4.M10] * two.val[Matrix4.M00] + one.val[Matrix4.M11] * two.val[Matrix4.M10] + one.val[Matrix4.M12] * two.val[Matrix4.M20] + one.val[Matrix4.M13] * two.val[Matrix4.M30];
		tmp[Matrix4.M11] = one.val[Matrix4.M10] * two.val[Matrix4.M01] + one.val[Matrix4.M11] * two.val[Matrix4.M11] + one.val[Matrix4.M12] * two.val[Matrix4.M21] + one.val[Matrix4.M13] * two.val[Matrix4.M31];
		tmp[Matrix4.M12] = one.val[Matrix4.M10] * two.val[Matrix4.M02] + one.val[Matrix4.M11] * two.val[Matrix4.M12] + one.val[Matrix4.M12] * two.val[Matrix4.M22] + one.val[Matrix4.M13] * two.val[Matrix4.M32];
		tmp[Matrix4.M13] = one.val[Matrix4.M10] * two.val[Matrix4.M03] + one.val[Matrix4.M11] * two.val[Matrix4.M13] + one.val[Matrix4.M12] * two.val[Matrix4.M23] + one.val[Matrix4.M13] * two.val[Matrix4.M33];
		
		tmp[Matrix4.M20] = one.val[Matrix4.M20] * two.val[Matrix4.M00] + one.val[Matrix4.M21] * two.val[Matrix4.M10] + one.val[Matrix4.M22] * two.val[Matrix4.M20] + one.val[Matrix4.M23] * two.val[Matrix4.M30];
		tmp[Matrix4.M21] = one.val[Matrix4.M20] * two.val[Matrix4.M01] + one.val[Matrix4.M21] * two.val[Matrix4.M11] + one.val[Matrix4.M22] * two.val[Matrix4.M21] + one.val[Matrix4.M23] * two.val[Matrix4.M31];
		tmp[Matrix4.M22] = one.val[Matrix4.M20] * two.val[Matrix4.M02] + one.val[Matrix4.M21] * two.val[Matrix4.M12] + one.val[Matrix4.M22] * two.val[Matrix4.M22] + one.val[Matrix4.M23] * two.val[Matrix4.M32];
		tmp[Matrix4.M23] = one.val[Matrix4.M20] * two.val[Matrix4.M03] + one.val[Matrix4.M21] * two.val[Matrix4.M13] + one.val[Matrix4.M22] * two.val[Matrix4.M23] + one.val[Matrix4.M23] * two.val[Matrix4.M33];
		
		tmp[Matrix4.M30] = one.val[Matrix4.M30] * two.val[Matrix4.M00] + one.val[Matrix4.M31] * two.val[Matrix4.M10] + one.val[Matrix4.M32] * two.val[Matrix4.M20] + one.val[Matrix4.M33] * two.val[Matrix4.M30];
		tmp[Matrix4.M31] = one.val[Matrix4.M30] * two.val[Matrix4.M01] + one.val[Matrix4.M31] * two.val[Matrix4.M11] + one.val[Matrix4.M32] * two.val[Matrix4.M21] + one.val[Matrix4.M33] * two.val[Matrix4.M31];
		tmp[Matrix4.M32] = one.val[Matrix4.M30] * two.val[Matrix4.M02] + one.val[Matrix4.M31] * two.val[Matrix4.M12] + one.val[Matrix4.M32] * two.val[Matrix4.M22] + one.val[Matrix4.M33] * two.val[Matrix4.M32];
		tmp[Matrix4.M33] = one.val[Matrix4.M30] * two.val[Matrix4.M03] + one.val[Matrix4.M31] * two.val[Matrix4.M13] + one.val[Matrix4.M32] * two.val[Matrix4.M23] + one.val[Matrix4.M33] * two.val[Matrix4.M33];
	}
	

	public static void setToRotation(float rad, Stone<?, ?> target){
		Roll roll = tmpRoll.get();
		//Important previously to set zero for modeled stones
		roll.setRotation(0);
		roll.setRotation(rad);
		for(Satellite satellite : target.getSatellites()){
			satellite.rotate(roll, target.origin());
		}
		target.adjustCurrentPrint();
	}
	
}
