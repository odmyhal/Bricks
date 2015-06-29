package org.bricks.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.prefs.Preferences;

import org.bricks.engine.pool.District;
import org.bricks.engine.pool.World;
import org.bricks.engine.event.handler.EventHandleRegistrator;
import org.bricks.engine.event.overlap.OverlapStrategyRegistrator;

public class Engine<R> 
{
	private World<R> world;
	private Motor[] motors;
	private ExecutorService service;
	public static Preferences preferences;
	
	public void init(Preferences props){
		preferences = props;
		this.world = new World<R>(props);
		int motorCount = props.getInt("motor.count", 1);
		this.motors = new Motor[motorCount];
		for(int i = 0; i < motorCount; i++){
			this.motors[i] = new Motor();
		}
		
		initEventHandlers(this.preferences);
		initOverlapStrategies(this.preferences);
		
	}
	
	public World<R> getWorld(){
		return world;
	}
	
	private void initEventHandlers(Preferences props){
		EventHandleRegistrator ehr = null;
		try {
			Class<EventHandleRegistrator> eClass = (Class<EventHandleRegistrator>) Class.forName(props.get("event.handel.registrator", "org.bricks.engine.event.handler.EmptyEventHandlerRegistrator"));
			Constructor<EventHandleRegistrator> rConstructor = eClass.getConstructor(); 
			ehr = rConstructor.newInstance();
			ehr.registerEventHandlers();
		} catch (ClassNotFoundException e) {
			System.out.println("WARN (" + this.getClass().getCanonicalName() + ") : Could not find registrator class " + props.get("event.handel.registrator", "org.bricks.engine.event.handler.EmptyEventHandlerRegistrator"));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initOverlapStrategies(Preferences props){
		OverlapStrategyRegistrator osr = null;
		try {
			Class<OverlapStrategyRegistrator> eClass = (Class<OverlapStrategyRegistrator>) Class.forName(props.get("overlap.strategy.registrator", "org.bricks.engine.overlap.OSRegistrator"));
			Constructor<OverlapStrategyRegistrator> rConstructor = eClass.getConstructor(); 
			osr = rConstructor.newInstance();
			osr.registerStrategies();
		} catch (ClassNotFoundException e) {
			System.out.println("WARN (" + this.getClass().getCanonicalName() + ") : Could not find registrator class " + props.get("overlap.strategy.registrator", "org.bricks.engine.overlap.OSRegistrator"));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Motor getLazyMotor(){
		int minCap = 2048;
		Motor add = null;
		for(int i = 0; i < motors.length; i++){
			if(minCap > motors[i].capacity()){
				add = motors[i];
				minCap = add.capacity();
			}
		}
		return add;
	}
	
	public void start(){
		service = Executors.newFixedThreadPool(motors.length, new ThreadFactory(){
			
			private int motorNumber = 0;

//			@Override
			public Thread newThread(Runnable arg0) {
				Thread nThread = new Thread(arg0, "Motor-thread-" + (++motorNumber));
				nThread.setDaemon(true);
				return nThread;
			}
			
		});
		for(Motor motor : motors){
			service.execute(motor);
		}
	}
	
	public void stop(){
		for(Motor motor : motors){
			motor.stop();
		}
		service.shutdown();
	}
/*	
	public Collection<District<R, ?>> getDistricts(){
		return world.getDistricts();
	}
	*/
}
