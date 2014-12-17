package com.odmyhal.shoot;

import org.junit.Test;

import com.badlogic.gdx.math.Vector3;

public class VectorTest {

	@Test
	public void normal(){
		Vector3 v3 = new Vector3(5f, -15f, 20f);
		Vector3 n = v3.cpy().nor();
		n.x *= -1;
		n.y *= -1;
		n.z *= -1;
		System.out.println(String.format("Vector %s nas normal %s", v3, n));
	}
}
