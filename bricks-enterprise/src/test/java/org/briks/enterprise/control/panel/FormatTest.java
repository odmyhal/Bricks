package org.briks.enterprise.control.panel;

import org.junit.Test;

public class FormatTest {

	@Test
	public void one(){
		String s = String.format("Float is %.2f done", 0.256f);
		System.out.println(s);
	}
}
