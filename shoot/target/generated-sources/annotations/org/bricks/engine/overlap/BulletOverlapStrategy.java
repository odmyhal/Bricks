package org.bricks.engine.overlap;

import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.event.overlap.OverlapAlgorithm;
import org.bricks.engine.staff.Subject;

public class BulletOverlapStrategy extends OverlapStrategy<com.odmyha.weapon.Bullet>{

	public BulletOverlapStrategy(OverlapAlgorithm algorithm){
		super(algorithm);
	}
	
	public boolean hasToCheckOverlap(com.odmyha.weapon.Bullet target, Subject source){
		return target.checkCannonOverlap(source);
	}
}