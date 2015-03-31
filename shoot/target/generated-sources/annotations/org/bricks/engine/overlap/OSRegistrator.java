package org.bricks.engine.overlap;

import org.bricks.engine.event.overlap.OverlapStrategyRegistrator;
import org.bricks.engine.event.overlap.OSRegister;

public class OSRegistrator implements OverlapStrategyRegistrator{

	public void registerStrategies() {
			OSRegister.registerStrategy(com.odmyha.weapon.Bullet.class, "CannonSource@shoot.odmyha.com", 
			   new org.bricks.engine.overlap.BulletOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.shoot.Ball.class, "CannonSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.OverlapStrategy.TrueOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.weapon.Bullet.class, "ShieldSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.OverlapStrategy.TrueOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.shoot.Ball.class, "ShieldSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.OverlapStrategy.TrueOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.shoot.Ball.class, "BallSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.SmallEventStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.weapon.Cannon.class, "ShieldSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.OverlapStrategy.TrueOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.weapon.Cannon.class, "CannonSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.SmallEventStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
			OSRegister.registerStrategy(com.odmyha.weapon.Bullet.class, "BallSource@shoot.odmyha.com", 
			   new org.bricks.engine.event.overlap.OverlapStrategy.TrueOverlapStrategy(new org.bricks.engine.event.overlap.BrickOverlapAlgorithm()));
		}
}