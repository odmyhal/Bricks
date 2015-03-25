package org.bricks.extent.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MarkPoint {

	private Vector3[] marks;
	protected Vector3[] modifiedMarks;
	private Collection<Matrix4> transforms = new ArrayList<Matrix4>();
	private Matrix4 helpMatrix = new Matrix4();
	protected int size;
	
	public MarkPoint(Vector3... points){
		marks = points;
		modifiedMarks = new Vector3[marks.length];
		for(int i = 0; i < marks.length; i++){
			modifiedMarks[i] = new Vector3(marks[i].x, marks[i].y, marks[i].z);
		}
		size = marks.length;
	}
	
	public Vector3 getMark(int num){
		return modifiedMarks[num];
	}
	
	/**
	 * Global transform matrix should be added first!!!
	 * Because Vect3.mul(Matirix4) means: Matrix * Vector
	 * @param transform
	 */
	public void addTransform(Matrix4 transform){
		transforms.add(transform);
	}
	
	public void calculateTransforms(){
		helpMatrix.idt();
		for(Matrix4 transform : transforms){
			helpMatrix.mul(transform);
		}
		final float l_mat[] = helpMatrix.val;
		for(int i = 0; i < marks.length; i++){
			float x = marks[i].x, y = marks[i].y, z = marks[i].z;
			modifiedMarks[i].set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03], x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13], x * l_mat[Matrix4.M20] + y
			* l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]);
		}
	}
}
