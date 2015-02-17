package org.bricks.engine.item;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointSetHelper;
import org.bricks.engine.Engine;
import org.bricks.engine.event.EventSource;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.pool.World;

@SuppressWarnings("rawtypes")
public abstract class Stone<S extends Subject, P extends EntityPrint> extends MultiSubjectEntity<S, P>{
	
	public Stone(S s){
		this.addSubject(s);
	}

	public Stone() {}

	@Override
	public void applyEngine(Engine engine) {
		super.applyEngine(engine);
		World world = engine.getWorld();
		for(Subject subject : getStaff()){
			subject.adjustCurrentPrint();
			Dimentions dimm = PointSetHelper.fetchDimentions(subject.getBrick().getPoints());
			int startRow = world.detectSectorRow(dimm.getMinYPoint()) - 1;
			int startCol = world.detectSectorCol(dimm.getMinXPoint()) - 1;
			int endRow = world.detectSectorRow(dimm.getMaxYPoint()) + 1;
			int endCol = world.detectSectorCol(dimm.getMaxXPoint()) + 1;
			for(int row = startRow; row <= endRow; row++){
				for(int col = startCol; col <= endCol; col++){
					District cur = world.getDistrict(row, col);
					if(cur != null){
						if(OverlapChecker.isOvarlap(cur.getPrint(), subject.getInnerPrint())){
							subject.joinPool(cur);
							subject.joinPool(cur.getBuffer());
						}else if(OverlapChecker.isOvarlap(cur.getBuffer().getPrint(), subject.getInnerPrint())){
							subject.joinPool(cur.getBuffer());
						}
					}
				}
			}
		}
	}
	

//	@Override
	public boolean isEventTarget(){
		return Boolean.FALSE;
	}

}
