package org.bricks.utils;

import java.util.Iterator;

public interface Loop<T> extends Iterable<T> {

	public boolean add(T val);
	public boolean remove(T val);
	public void clear();
	public boolean contains(T val);
	public boolean isEmpty();
}
