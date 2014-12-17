package com.odmyha.subject;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.pool.Subject;

import com.odmyha.weapon.Bullet;
import com.odmyha.weapon.Cannon;

@Deprecated
public class GunSubject extends Subject<Cannon>{
	
	private Ipoint firePoint;

	public GunSubject(Brick brick, Ipoint firePoint) {
		super(brick);
		this.firePoint = firePoint;
	}

	@Override
	public void translate(int x, int y){
		super.translate(x, y);
		firePoint.translate(x, y);
	}
	
	public void rotate(float rad, Point central){
		super.rotate(rad, central);
	}
}
