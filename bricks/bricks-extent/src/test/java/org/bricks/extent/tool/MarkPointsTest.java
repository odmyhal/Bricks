package org.bricks.extent.tool;

import org.junit.Test;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MarkPointsTest {

	@Test
	public void oneTest(){
		MarkPoint mp = new MarkPoint(new Vector3(10f, 0f, 0f));
		
		Matrix4 m3 = new Matrix4();
		m3.setToRotation(0f,  0f, 99f, -90f);
		mp.addTransform(m3);
		
		Matrix4 m2 = new Matrix4();
		m2.setToRotation(-90f, 0f, 0f, -90f);
		mp.addTransform(m2);
		
		Matrix4 m1 = new Matrix4();
		m1.setToRotation(0f, -99f, 0f, 90f);
		mp.addTransform(m1);
		
		System.out.println("Before calculation");
		mp.calculateTransforms();
		
		System.out.println(mp.getMark(0));
	}
}
