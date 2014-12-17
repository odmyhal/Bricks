package org.bricks.core.entity;

import java.util.LinkedList;
import java.util.Vector;

@Deprecated
public class PointPool {
	
	
	private static final ThreadLocal<PointPool> threadPointCache = new ThreadLocal<PointPool>(){
		@Override
		protected PointPool initialValue(){
			return new PointPool();
		}
	};
	
	private Vector<Cpoint> tmpCache = new Vector<Cpoint>();
	private LinkedList<Cpoint> pointCache = new LinkedList<Cpoint>();
	private Cpoint fetchPoint(float x, float y){
		Cpoint r = pointCache.poll();
		if(r == null){
			r = new Cpoint(x, y, tmpCache);
		}
		r.setX(x);
		r.setY(y);
		return r;
	}
	
	private void flushTmp(){
		pointCache.addAll(tmpCache);
		tmpCache.clear();
	}
	
	public static Cpoint providePoint(float x, float y){
		return threadPointCache.get().fetchPoint(x, y);
	}
	
	public static void refresh(){
		threadPointCache.get().flushTmp();
	}

}
