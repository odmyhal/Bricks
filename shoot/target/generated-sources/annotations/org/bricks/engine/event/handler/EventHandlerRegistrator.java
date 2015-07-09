package org.bricks.engine.event.handler;

public class EventHandlerRegistrator implements EventHandleRegistrator{

	public void registerEventHandlers() {
			EventHandlerManager.registerHandler(com.odmyha.weapon.Cannon.class, org.bricks.engine.event.PrintOverlapEvent.class, "CannonSource@shoot.odmyha.com", new CannonOverlapEventHandler());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.BorderEvent.class, "SideBorder@engine.bricks.org", new BallBorderEventHandler());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Bullet.class, org.bricks.engine.event.PrintOverlapEvent.class, "CannonSource@shoot.odmyha.com", new BulletOverlapEventHandler_2());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Bullet.class, org.bricks.engine.event.PrintOverlapEvent.class, "BallSource@shoot.odmyha.com", new BulletOverlapEventHandler_1());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Cannon.class, org.bricks.engine.event.PrintOverlapEvent.class, "BallSource@shoot.odmyha.com", new CannonOverlapEventHandler_1());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Cannon.class, org.bricks.engine.event.PrintOverlapEvent.class, "ShieldSource@shoot.odmyha.com", new CannonOverlapEventHandler_2());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.BorderEvent.class, "CorneBorder@engine.bricks.org", new BallBorderEventHandler_1());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.PrintOverlapEvent.class, "ShieldSource@shoot.odmyha.com", new BallOverlapEventHandler_1());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.PrintOverlapEvent.class, "BallSource@shoot.odmyha.com", new BallOverlapEventHandler());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.PrintOverlapEvent.class, "BulletSource@shoot.odmyha.com", new BallOverlapEventHandler_3());
			EventHandlerManager.registerHandler(com.odmyha.shoot.Ball.class, org.bricks.engine.event.PrintOverlapEvent.class, "CannonSource@shoot.odmyha.com", new BallOverlapEventHandler_2());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Bullet.class, org.bricks.engine.event.PrintOverlapEvent.class, "ShieldSource@shoot.odmyha.com", new BulletOverlapEventHandler());
			EventHandlerManager.registerHandler(com.odmyha.weapon.Cannon.class, org.bricks.extent.event.FireEvent.class, "UserSourceType@extent.bricks.org", new CannonFireEventHandler());
		}
}