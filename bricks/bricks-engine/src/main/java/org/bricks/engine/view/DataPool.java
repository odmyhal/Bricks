package org.bricks.engine.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.bricks.engine.staff.Entity;

public class DataPool<E> extends DurableView implements Iterable<E>{
	
	private final HashSet<E> entities = new HashSet<E>();

	public DataPool(LinkedList backet) {
		super(backet);
	}
	
	public void addAll(DataPool<E> de){
		entities.addAll(de.entities);
	}
	
	public void addAll(Collection<E> collection){
		entities.addAll(collection);
	}
	
	public void clear(){
		entities.clear();
	}
	
	public boolean add(E e){
		return entities.add(e);
	}
	
	public boolean remove(E e){
		return entities.remove(e);
	}

//	@Override
	public Iterator<E> iterator() {
		return entities.iterator();
	}
	
	public void free(){
		entities.clear();
		super.free();
	}
	
	public HashSet<E> getEntities(){
		return entities;
	}

}
