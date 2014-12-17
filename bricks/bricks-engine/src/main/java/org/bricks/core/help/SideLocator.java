package org.bricks.core.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;


public class SideLocator {
	
	private static SectorPointSearcher[] searcher = new SectorPointSearcher[5];
	
	static{
		searcher[1] = new SectorPointSearcher(){

			@Override
			protected boolean includeCondition(Point p, Point center) {
				return p.getFX() > center.getFX();
			}
			
		};
		
		searcher[2] = new SectorPointSearcher(){

			@Override
			protected boolean includeCondition(Point p, Point center) {
				return p.getFY() > center.getFY();
			}
			
		};
		
		searcher[3] = new SectorPointSearcher(){

			@Override
			protected boolean includeCondition(Point p, Point center) {
				return p.getFX() < center.getFX();
			}
			
		};
		
		searcher[4] = new SectorPointSearcher(){

			@Override
			protected boolean includeCondition(Point p, Point center) {
				return p.getFY() < center.getFY();
			}
			
		};
		
		for(int k = 1; k < 5; k++){
			searcher[k].setSectorNum(k);
		}
	}
	
	public static Collection<Point> findPointsOfSector(Collection<? extends Point> points, Point center, int sideNum){
		return searcher[sideNum].searchForPoints(points, center);
	}

	private static abstract class SectorPointSearcher{
		
		protected int sectorNum;
		protected abstract boolean includeCondition(Point p, Point center);
		
		protected void setSectorNum(int sNum){
			this.sectorNum = sNum;
		}
		
		protected Collection<Point> searchForPoints(Collection<? extends Point> points, Point center){
			Collection<Point> res = new ArrayList<Point>();
			boolean cond, prevCond = false; 
			boolean started = false;
			Point one = null, two = null;
			Iterator<? extends Point> pIter = points.iterator();
			while(true){
				if(!pIter.hasNext()){
					pIter = points.iterator();
				}
				if(started){
					two = pIter.next();
					cond = includeCondition(two, center);
					res.add(two);
					if(prevCond && !cond){
						break;
					}
				}else{
					if(one == null){
						one = pIter.next();
						prevCond = includeCondition(one, center);
					}
					two = pIter.next();
					cond = includeCondition(two, center);
					if(!prevCond && cond){
						started = true;
						res.add(one);
						res.add(two);
					}
				}
				prevCond = cond;
				one = two;
			}
			return res;
		}
	}
}
