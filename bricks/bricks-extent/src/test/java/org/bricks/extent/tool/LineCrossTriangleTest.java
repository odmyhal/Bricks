package org.bricks.extent.tool;

import org.bricks.extent.space.overlap.MBCrossLineAlgorithm;
import org.bricks.extent.space.overlap.Triangle;
import org.junit.Test;

import com.badlogic.gdx.math.Vector3;

public class LineCrossTriangleTest {

	@Test
	public void one(){
		
		Triangle triangle = new Triangle(new Vector3(0f, 0f, 0f), new Vector3(100f, 0f, 100f), new Vector3(0f, 100f, 100f));
		Vector3 origin1 = new Vector3(0f, 0f, 100f);
		Vector3 move = new Vector3(-70f, -70f, 70f);
		
		MBCrossLineAlgorithm algorithm = new MBCrossLineAlgorithm();
	/*	
		float k = algorithm.lineCrossTriangle(triangle, origin1, move);
		
		System.out.println("Got result1: " + k);
	*/	
		triangle = new Triangle(new Vector3(0f,0f, 0f), new Vector3(100f, 0f, 100f), new Vector3(0f, 100f, 100f));

		origin1 = new Vector3(25f, -20f, 75f);
		move = new Vector3(0f, -140f, 0f);
		
//		MBCrossLineAlgorithm algorithm = new MBCrossLineAlgorithm();
		
		float k1 = algorithm.lineCrossTriangle(triangle, origin1, move);
		
		System.out.println("Got result2: " + k1);
/*		
		triangle = new Triangle(new Vector3(0f,0f, 0f), new Vector3(100f, 0f, 0f), new Vector3(0f, 100f, 0f));

		origin1 = new Vector3(25f, 25f, -10f);
		move = new Vector3(0f, 0f, -40f);
		
//		MBCrossLineAlgorithm algorithm = new MBCrossLineAlgorithm();
		
		float k3 = algorithm.lineCrossTriangle(triangle, origin1, move);
		
		System.out.println("Got result2: " + k3);*/
	}
}
