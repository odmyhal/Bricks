package org.bricks.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import org.bricks.engine.item.Motorable;
import org.bricks.engine.tool.Quarantine;

public class Motor implements Runnable {
	
	private volatile boolean run = true;
	private Collection<Motorable> alive = new ArrayList<Motorable>();
//	private Collection<Motorable> dead = new ArrayList<Motorable>();
	private Quarantine<Motorable> dead = new Quarantine<Motorable>(30);
//	private Collection<Motorable> added = new ArrayList<Motorable>();
	private Quarantine<Motorable> added = new Quarantine<Motorable>(30);
	private AtomicInteger capacity = new AtomicInteger(0);
	private volatile boolean wait = false;

	private long startTime, hooks;
	public void run() {
		startTime = System.currentTimeMillis();
		hooks = 0;
		System.out.println("Motor " + Thread.currentThread().getName() + " started...");
		while(run){
/*			synchronized(dead){
				alive.removeAll(dead);
				dead.clear();
			}*/
			for(Motorable mo : dead){
				alive.remove(mo);
			}
			for(Motorable mo : added){
				alive.add(mo);
			}
			if(alive.isEmpty()){
				synchronized(alive){
					wait = true;
					if(added.isEmpty()){
						try {
							alive.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						wait = false;
					}
				}
			}
/*			synchronized(added){
				alive.addAll(added);
				added.clear();
				if(alive.isEmpty()){
					try {
						added.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}*/
			long currentTime = System.currentTimeMillis();
			for(Motorable subject : alive){
				subject.motorProcess(currentTime);
			}
			Thread.currentThread().yield();
			hooks++;
		}
		long diffTime = System.currentTimeMillis() - startTime;
		System.out.println(String.format("Motor %s: average delay - %.3f", Thread.currentThread().getName(), (((double) diffTime) / hooks) ));
	}
	
	public void stop(){
		run = false;
	}
	
	public boolean addLiver(Motorable subject){
		capacity.incrementAndGet();
		added.push(subject);
		if(wait){
			synchronized(alive){
				if(wait){
					alive.notify();
					wait = false;
				}
			}
		}
		return true;
/*		synchronized(added){
			boolean res = added.add(subject);
			added.notify();
			return res;
		}*/
	}
	
	public boolean removeLiver(Motorable subject){
		capacity.decrementAndGet();
		dead.push(subject);
		return true;
/*		synchronized(dead){
			return dead.add(subject);
		}*/
	}
	
	public int capacity(){
		return capacity.get();
	}

}
