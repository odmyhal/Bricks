package org.bricks.engine.event.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bricks.engine.event.Event;
import org.bricks.engine.processor.Processor;
import org.bricks.engine.staff.Liver;

/**
 * 
 * @author oleh
 * This checker involves number of inner checkers 
 * which works sequentially: the next start works in chance of previous is deactivated(isActive = true)
 * After last inner checker finish it's job, main checker is removed from target object
 * @param <T>
 */
public class ChunkEventChecker<T extends Liver> extends EventChecker<T> {
	
	private List<EventChecker<T>> checkers = new ArrayList<EventChecker<T>>();
	protected int nextIndex = 0;
	private EventChecker<T> currentChecker;
	
	public ChunkEventChecker(CheckerType checkerType, EventChecker<T>...chrs){
		super(checkerType);
		checkers.addAll(Arrays.asList(chrs));
	}

	@Override
	protected Event popEvent(T target, long eventTime){
		while(currentChecker == null || !currentChecker.isActive()){
			if(checkers.size() > nextIndex){
				currentChecker = checkers.get(nextIndex++);
				currentChecker.activate(target, eventTime);
			}else{
				finish(target, eventTime);
				return null;
			}
		}
		return currentChecker.popEvent(target, eventTime);
	}
	
	protected void finish(T target, long eventTime){
		disable();
		target.unregisterEventChecker(this);
	}
	
	protected void reject(){
		nextIndex = 0;
	}
	
	protected boolean indexBack(){
		if(nextIndex > 0){
			--nextIndex;
			return true;
		}
		return false;
	}
	
	public boolean restartCurrentChecker(){
		if(indexBack()){
			currentChecker = null;
			return true;
		}
		return false;
	}
	
	@Override
	public void activate(T target, long curTime){
		currentChecker = null;
		nextIndex = 0;
		super.activate(target, curTime);
	}
	
	public boolean addChecker(EventChecker<T> checker){
		return checkers.add(checker);
	}
	
	public boolean addProcessor(Processor<T> processor){
		return checkers.add(processor);
	}
	
	public int size(){
		return checkers.size();
	}
	
}
