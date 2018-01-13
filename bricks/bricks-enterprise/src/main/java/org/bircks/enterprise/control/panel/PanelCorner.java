package org.bircks.enterprise.control.panel;

public enum PanelCorner {
	
	UP_RIGHT(1,1), 
	UP_LEFT(0, 1),
	BOTTOM_LEFT(0, 0),
	BOTTOM_RIGHT(1, 0);
	
	private int width, height;
	private PanelCorner(int w, int h){
		this.width = w;
		this.height = h;
	}
	
	public int width(){
		return this.width;
	}
	
	public int height(){
		return this.height;
	}
}
