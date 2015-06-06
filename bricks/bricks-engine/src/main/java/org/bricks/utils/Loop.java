package org.bricks.utils;

public interface Loop<T> extends Iterable<T> {

	public boolean add(T val);
	public boolean remove(T val);
	public void clear();
	public boolean contains(T val);
	public boolean isEmpty();
}
