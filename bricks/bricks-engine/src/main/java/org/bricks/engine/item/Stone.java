package org.bricks.engine.item;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.help.PointSetHelper;
import org.bricks.engine.Engine;
import org.bricks.engine.event.overlap.BrickOverlapAlgorithm;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.World;
import org.bricks.engine.tool.Origin;
import org.bricks.engine.tool.Origin2D;

@SuppressWarnings("rawtypes")
public abstract class Stone<S extends BrickSubject, P extends EntityPrint> extends MultiSubjectEntity<S, P, Fpoint>{
	
	public Stone(S s){
		this.addSubject(s);
	}

	public Stone() {}
	
	public Origin<Fpoint> provideInitialOrigin(){
		return new Origin2D();
	}

	@Override
	public void applyEngine(Engine engine) {
		super.applyEngine(engine);
		World world = engine.getWorld();
		for(BrickSubject<?, EntityPointsPrint> subject : getStaff()){
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
						if(BrickOverlapAlgorithm.isPointSetOverlap(cur.getPrint(), subject.getInnerPrint())){
							subject.joinPool(cur);
							subject.joinPool(cur.getBuffer());
						}else if(BrickOverlapAlgorithm.isPointSetOverlap(cur.getBuffer().getPrint(), subject.getInnerPrint())){
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
