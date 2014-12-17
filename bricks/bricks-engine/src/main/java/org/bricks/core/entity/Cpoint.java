package org.bricks.core.entity;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class Cpoint extends Fpoint{
	
	private static AtomicInteger ai = new AtomicInteger();

	private volatile Vector<Cpoint> cache;
	public Cpoint(float x, float y, Vector<Cpoint> v){
		super(x, y);
		this.cache = v;
		ai.incrementAndGet();
	}
	
	public void cache(){
		cache.add(this);
	}
	
	public static int getTotal(){
		return ai.get();
	}

}
