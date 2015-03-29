package org.bricks.engine.pool;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.staff.Subject;

public class SectorMonitor {
	
	private static int provideSectorMask(Subject subject){
		int curMask = 0;
		Point point = subject.getCenter();
		District sector = subject.getDistrict();
		Ipoint dimm = sector.getDimentions();
		Ipoint corner = sector.getCorner();
		int luft = sector.getLuft();
		
		if(point.getX() < corner.getX()){
			curMask += 64;
		}else if(point.getX() < corner.getX() + luft){
			curMask += 4;
		}else if(point.getX() >= corner.getX() + dimm.getFX()){
			curMask += 16;
		}else if(point.getX() >= corner.getX() + dimm.getFX() - luft){
			curMask += 1;
		}
		
		if(point.getY() < corner.getY()){
			curMask += 128;
		}else if(point.getY() < corner.getY() + luft){
			curMask += 8;
		}else if(point.getY() >= corner.getY() + dimm.getY()){
			curMask += 32;
		}else if(point.getY() >= corner.getY() + dimm.getY() - luft){
			curMask += 2;
		}
		return curMask;
	}
	
	public static void monitor(Subject subject){
		int curMask = provideSectorMask(subject);
		if(curMask > 15){
//			System.out.println("The mask is " + curMask + ", subject Point: " + subject.getPoint());
			District sector = subject.getDistrict();
			District newSector = sector.getWorld().pointSector(subject.getCenter());
			if(newSector == null){
//				System.out.println("Out of world: " + subject.getEntity().getClass().getSimpleName() + ", center " + subject.getCenter());
				subject.getEntity().outOfWorld();
				return;
			}
//			Validate.isTrue(newSector != null, "No sector for point " + subject.getCenter());
			if(sector.equals(newSector)){
				System.out.println("Subject point: " + subject.getCenter());
				System.out.println("Secondary enter new distric: " + newSector);
			}
/*			sector.lockView();
			newSector.lockView();
			subject.leaveDistrict();
			subject.joinDistrict(newSector);
			sector.unlockView();
			newSector.unlockView();*/
			subject.moveToDistrict(newSector);
			return;
		}
		int mask = subject.getDistrictMask();
		if(curMask != mask){
			int comm = curMask & mask;
			int del = mask - comm;
			int add = curMask - comm;
			if(add > 0){
				if((add & 1) > 0){
					joinCloseArea(subject, 1, 0);
					if(curMask == 3){
						joinCloseArea(subject, 1, 1);
					}else if(curMask == 9){
						joinCloseArea(subject, 1, -1);
					}
				}else if((add & 4) > 0){
					joinCloseArea(subject, -1, 0);
					if(curMask == 6){
						joinCloseArea(subject, -1, 1);
					}else if(curMask == 12){
						joinCloseArea(subject, -1, -1);
					}
				}
				if((add & 2) > 0){
					joinCloseArea(subject, 0, 1);
					if(curMask == 3){
						joinCloseArea(subject, 1, 1);
					}else if(curMask == 6){
						joinCloseArea(subject, -1, 1);
					}
				}else if((add & 8) > 0){
					joinCloseArea(subject, 0, -1);
					if(curMask == 9){
						joinCloseArea(subject, 1, -1);
					}else if(curMask == 12){
						joinCloseArea(subject, -1, -1);
					}
				}
			}
			if(del > 0){
				if((del & 1) > 0){
					leaveCloseArea(subject, 1, 0);
					if(mask == 3){
						leaveCloseArea(subject, 1, 1);
					}else if(mask == 9){
						leaveCloseArea(subject, 1, -1);
					}
				}else if((del & 4) > 0){
					leaveCloseArea(subject, -1, 0);
					if(mask == 6){
						leaveCloseArea(subject, -1, 1);
					}else if(mask == 12){
						leaveCloseArea(subject, -1, -1);
					}
				}
				if((del & 2) > 0){
					leaveCloseArea(subject, 0, 1);
					if(mask == 3){
						leaveCloseArea(subject, 1, 1);
					}else if(mask == 6){
						leaveCloseArea(subject, -1, 1);
					}
				}else if((del & 8) > 0){
					leaveCloseArea(subject, 0, -1);
					if(mask == 9){
						leaveCloseArea(subject, 1, -1);
					}else if(mask == 12){
						leaveCloseArea(subject, -1, -1);
					}
				}
			}
		}
		subject.setDistrictMask(curMask);
	}
	
	private static boolean joinCloseArea(Subject subject, int diffX, int diffY){
		District sector = subject.getDistrict();
		World w = sector.getWorld();
		District neighbor = w.getDistrict(sector.rowNumber + diffY, sector.colNumber + diffX);
		if(neighbor == null){
			return false;
		}
		return subject.joinPool(neighbor.getBuffer());
	}
	
	private static boolean leaveCloseArea(Subject subject, int diffX, int diffY){
		District sector = subject.getDistrict();
		World w = sector.getWorld();
		District neighbor = w.getDistrict(sector.rowNumber + diffY, sector.colNumber + diffX);
		if(neighbor == null){
			return false;
		}
		return subject.leavePool(neighbor.getBuffer());
	}

}
