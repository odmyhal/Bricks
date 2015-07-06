package org.bricks.utils;

import java.util.Iterator;
import java.util.LinkedList;
import org.bricks.exception.Validate;
import org.bricks.utils.Cache.LocalCache;

public class LinkLoop<T> implements Loop<T>{
	
	private static final LinkLoop linkLoop = new LinkLoop();
	private static final ThreadLocal<LoopIteratorProvider> iteratorProvider = new ThreadLocal<LoopIteratorProvider>(){
		@Override protected LoopIteratorProvider initialValue() {
			return new LoopIteratorProvider();
	    }
	};
	
	private Link first;
	private Link last;

	private LinkedList<Link> cache = new LinkedList<Link>();
	
	protected LoopIterator defaultIterator(){
		return iteratorProvider.get().getIterator();
	}


	public final Iterator<T> iterator() {
		LoopIterator iterator = defaultIterator();
	//	Validate.isFalse(iterator == null, "Found null LoopIterator for thread " + Thread.currentThread().getName());
		iterator.init(this);
		return iterator;
	}
	

	protected Link linkInstance(){
		Link instance = cache.poll();
		if(instance == null){
			instance = new Link();
		}
		return instance;
	}
	

	public boolean add(T val) {
		Link link = findLinkOfVal(val);
		if(link == null){
			link = linkInstance();
			link.setValue(val);
			link.attach();
			return true;
		}
//		link.setValue(val);
		return false;
		
	}

	public boolean remove(T val) {
		Link link = findLinkOfVal(val);
		if(link == null){
			return false;
		}
		link.utilize();
		return true;
	}

	public void clear() {
		Link l = first;
		while(l != null){
			Link next = l.next;
			l.clean();
			l = next;
		}
		first = last = null;
/*		Iterator<T> it = this.iterator();
		while(it.hasNext()){
			it.next();
			it.remove();
		}*/
	}

	public boolean contains(T val) {
		return findLinkOfVal(val) != null;
	}
	
	protected Link findLinkOfVal(T val){
		Link check = first;
		while(check != null){
			if(check.value.equals(val)){
				return check;
			}
			check = check.next;
		}
		return null;
	}

	public boolean isEmpty() {
		return first == null;
	}
	
	protected class Link{
		
		private Link previous;
		private Link next;
		private T value;
		
		protected void utilize(){
			if(previous == null){
				first = this.next;
			}else{
				previous.next = this.next;
			}
			if(next == null){
				last = this.previous;
			}else{
				next.previous = this.previous;
			}
			clean();
		}
		
		private void clean(){
			this.next = null;
			this.previous = null;
			this.value = null;
			cache.add(this);
		}
		
		protected void setValue(T val){
			this.value = val;
		}
		
		protected T getValue(){
			return this.value;
		}
		
		protected void attach(){
			if(last == null){
				first = this;
			}else{
				last.next = this;
				this.previous = last;
			}
			last = this;
		}
	}
	
	protected static class LoopIterator implements Iterator{
		
		private LinkLoop.Link tmp = linkLoop.linkInstance();
		private LinkLoop.Link current;
		
		public void init(LinkLoop ll){
			current = tmp;
			current.next = ll.first;
		}

		public boolean hasNext() {
			if(current.next == null){
				cacheIterator();
				return false;
			}
			return true;
		}
		
		protected void cacheIterator(){
			iteratorProvider.get().cacheIterator(this);
		}

		public Object next() {
			current = current.next;
			return current.value;
		}

		public void remove() {
			Validate.isFalse(tmp == current, "Not selected element to remove, call next() previously");
			LinkLoop.Link nextCur = current.previous;
			if(nextCur == null){
				nextCur = tmp;
				nextCur.next = current.next;
			}
			current.utilize();
			current = nextCur;
		}
	}
	
	protected static class LoopIteratorProvider implements Cache.DataProvider<LoopIterator>{
		
		private LocalCache<LoopIterator> iteratorCache = new ThreadScopeCache<LoopIterator>(this);

		public LoopIterator provideNew() {
			return new LoopIterator();
		}
		
		protected LoopIterator getIterator(){
			return iteratorCache.getLocal();
		}
		
		protected void cacheIterator(LoopIterator iterator){
			iteratorCache.putLocal(iterator);
		}
	}
}
