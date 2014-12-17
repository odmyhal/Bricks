package org.bricks.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.item.Motorable;
import org.bricks.engine.pool.SectorMonitor;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.staff.Liver;

public class Motor implements Runnable {
	
	private volatile boolean run = true;
	private Collection<Motorable> alive = new ArrayList<Motorable>();
	private Collection<Motorable> dead = new ArrayList<Motorable>();
	private Collection<Motorable> added = new ArrayList<Motorable>();
	private AtomicInteger capacity = new AtomicInteger(0);

	private long startTime, hooks;
	public void run() {
		startTime = System.currentTimeMillis();
		hooks = 0;
		System.out.println("Motor " + Thread.currentThread().getName() + " started...");
		while(run){
			synchronized(dead){
				alive.removeAll(dead);
				dead.clear();
			}
			synchronized(added){
				alive.addAll(added);
				added.clear();
				if(alive.isEmpty()){
					try {
						added.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			long currentTime = System.currentTimeMillis();
			for(Motorable subject : alive){
				subject.motorProcess(currentTime);
			}
			Thread.currentThread().yield();
			hooks++;
		}
		long diffTime = System.currentTimeMillis() - startTime;
		System.out.println(String.format("Motor %s: average delay - %d", Thread.currentThread().getName(), (diffTime / hooks) ));
	}
	
	public void stop(){
		run = false;
	}
	
	public boolean addSubject(Motorable subject){
		capacity.incrementAndGet();
		synchronized(added){
			boolean res = added.add(subject);
			added.notify();
			return res;
		}
	}
	
	public boolean removeSubject(Motorable subject){
		capacity.decrementAndGet();
		synchronized(dead){
			return dead.add(subject);
		}
	}
	
	public int capacity(){
		return capacity.get();
	}

}
