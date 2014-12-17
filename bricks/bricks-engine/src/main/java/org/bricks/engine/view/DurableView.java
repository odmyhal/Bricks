package org.bricks.engine.view;

import java.util.LinkedList;

public abstract class DurableView {
	
	private LinkedList backet;
	private int useCount;
	
	public DurableView(LinkedList backet){
		this.backet = backet;
	}
	
	public void occupy(){
		synchronized(backet){
			useCount++;
		}
	}
	
	public void free(){
		synchronized(backet){
			if(--useCount == 0){
				endUse();
			}
		}
	}
	
	protected void endUse(){
		backet.addLast(this);
	}

}
