package org.bricks.enterprise.gen;

import org.bircks.entierprise.model.ModelConstructTool;

public class GenModelConstructTool extends ModelConstructTool{
	
	protected void constructModels(){
				com.odmyha.model.construct.CannonConstructor.instance().construct(this, "tower", "tube");
				com.odmyha.model.construct.ShieldConstructor.instance().construct(this, "shield");
				com.odmyha.model.construct.BulletConstructor.instance().construct(this, "bullet");
				com.odmyha.model.construct.BallConstructor.instance().construct(this, "ball_60", "ball_30");
			}
}