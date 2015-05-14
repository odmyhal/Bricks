package org.bricks.core.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Point;


public class TangPointLocator {

private static TangSectorPointSearcher[] searcher = new TangSectorPointSearcher[5];
	
	/**
	 * Each sector comprises all points, except those which obviously belongs to opposite sector
	 */
	static{
		searcher[1] = new TangSectorPointSearcher(){
			@Override
			protected boolean apply(Point one, Point two) {
				return (one.getFX() > two.getFX() || one.getFY() <= two.getFY()) 
						&& (one.getFY() < two.getFY() || one.getFX() >= two.getFX());
			}
		};
		searcher[2] = new TangSectorPointSearcher(){
			@Override
			protected boolean apply(Point one, Point two) {
				return (one.getFY() > two.getFY() || one.getFX() >= two.getFX())
						&& (one.getFX() > two.getFX() || one.getFY() >= two.getFY());
			}
		};
		searcher[3] = new TangSectorPointSearcher(){
			@Override
			protected boolean apply(Point one, Point two) {
				return (one.getFX() < two.getFX() || one.getFY() >= two.getFY())
						&& (one.getFY() > two.getFY() || one.getFX() <= two.getFX());
			}
		};
		searcher[4] = new TangSectorPointSearcher(){
			@Override
			protected boolean apply(Point one, Point two) {
				return (one.getFX() < two.getFX() || one.getFY() <= two.getFY())
						&& (one.getFY() < two.getFY() || one.getFX() <= two.getFX());
			}
		};
		
		for(int k = 1; k < 5; k++){
			searcher[k].setSectorNum(k);
		}
	}
	
	public static Collection<Point> findPointsOfSector(Collection<? extends Point> points, int sectorNum, Collection<Point> dest){
		return searcher[sectorNum].searchForPoints(points, dest);
	}

	private static abstract class TangSectorPointSearcher{
		
		protected int sectorNum;
		
		protected abstract boolean apply(Point one, Point two);
		
		protected void setSectorNum(int sNum){
			this.sectorNum = sNum;
		}
		
		protected Collection<Point> searchForPoints(Collection<? extends Point> points, Collection<Point> dest){
//			Collection<Point> res = new ArrayList<Point>();
			dest.clear();
			boolean cond, prevCond = false; 
			boolean started = false;
			Point one = null, two = null, first = null;
			Iterator<? extends Point> pIter = points.iterator();
			int VALIDATE = 0;
			while(true){
				if(!pIter.hasNext()){
					pIter = points.iterator();
				}
				if(one == null){
					first = pIter.next();
					one = pIter.next();
					prevCond = apply(first, one);
				}
				two = pIter.next();
				cond = apply(one, two);
				if(started){
					if(cond){
						dest.add(two);
						if(two.equals(first)){
							break;
						}
					}else{
						break;
					}
				}else{
					if(one.equals(first)){
						prevCond = false;
					}
					if(cond && !prevCond){
						dest.add(one);
						dest.add(two);
						started = true;
						first = one;
					}
				}
				prevCond = cond;
				one = two;
				Validate.isTrue(++VALIDATE < 500);
			}
			return dest;
		}
	}
}
