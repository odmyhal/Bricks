package org.bricks.engine;

import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.pool.District;
import org.bricks.utils.HashLoop;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.LoopMap;
import org.junit.Test;

public class UtilTest {

	@Test
	public void sectorMonitorTest(){
		int curMask = 0;
		Point point = new Fpoint(6176.58496f, 4999.63330f);
//		District sector = subject.getDistrict();
		Ipoint dimm = new Ipoint(5000, 5000);
		Ipoint corner = new Ipoint(5000, 0);
		int luft = 1200;
		
		if(point.getFX() < corner.getX()){
			curMask += 64;
		}else if(point.getFX() < corner.getX() + luft){
			curMask += 4;
		}else if(point.getFX() >= corner.getX() + dimm.getFX()){
			curMask += 16;
		}else if(point.getFX() >= corner.getX() + dimm.getFX() - luft){
			curMask += 1;
		}
		
		if(point.getFY() < corner.getY()){
			curMask += 128;
		}else if(point.getFY() < corner.getY() + luft){
			curMask += 8;
		}else if(point.getFY() >= corner.getY() + dimm.getY()){
			curMask += 32;
		}else if(point.getFY() >= corner.getY() + dimm.getY() - luft){
			curMask += 2;
		}
		System.out.println("Found mask " + curMask);
	}
}
