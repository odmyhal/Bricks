package org.bricks.engine;

import org.junit.Test;

public class InTest {
	
//	@Test
	public void doubleTest(){
		int one = 10;
		double t = 0.568;
		double r = one - t;
		System.out.println("Result r is: " + r);
		System.out.println("Simple substraction: " + (one - t));
	}
	
	@Test
	public void sclTest(){
		System.out.println("Math.scalb(3,1) = " + Math.scalb(3f, 1));
		System.out.println("Math.scalb(3,2) = " + Math.scalb(3f, 2));
	}
}
