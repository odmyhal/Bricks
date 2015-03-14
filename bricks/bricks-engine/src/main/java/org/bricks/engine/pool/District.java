package org.bricks.engine.pool;

import java.util.ArrayList;
import java.util.Iterator;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.staff.Entity;

public final class District<R, E extends Entity> extends AreaBase<E> implements Iterable<R> {
	
	private Area buffer;
	private World<E> world;
	public int rowNumber, colNumber;
	private int luft;
//	private int viewLock;
	private volatile ArrayList<Boundary> boundaries;
	private final ThreadLocal<EntityIterator> localIterator = new ThreadLocal<EntityIterator>(){
		@Override protected EntityIterator initialValue() {
            return new EntityIterator();
        }
	};
	
//	private final LinkedList<DataPool<E>> districtEntities = new LinkedList<DataPool<E>>();
//	private DataPool<E> currentEntities = new DataPool<E>(districtEntities); 
	
	public District(Ipoint corner, int capacity, int len, int bufferLuft, World world){
		super(corner, capacity, len);
		Ipoint bufferCorner = new Ipoint(corner.getX() - bufferLuft, corner.getY() - bufferLuft);
		buffer = new Area(bufferCorner, (int) (Math.round(capacity * 1.2)), len + 2 * bufferLuft);
		rowNumber = corner.getY() / len;
		colNumber = corner.getX() / len;
		luft = bufferLuft;
		this.world = world;
	}

	public Area getBuffer() {
		return buffer;
	}
	
	public int getLuft(){
		return this.luft;
	}
	
	public World getWorld(){
		return world;
	}
/*
	public void lockView(){
		synchronized(districtEntities){
			viewLock++;
		}
	}
	//wait should be used in some method like getView
	public void unlockView(){
		synchronized(districtEntities){
			if(--viewLock <= 0){
				districtEntities.notifyAll();
			}
		}
	}
	
	private DataPool<E> getEntities(){
		synchronized(districtEntities){
			if(viewLock > 0){
				try {
					districtEntities.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DataPool<E> di = districtEntities.poll();
			if(di == null){
				di = new DataPool<E>(districtEntities);
			}
			di.occupy();
			di.addAll(currentEntities);
			return di;
		}
	}
	
	public DataPool<R> getRenderEntities(){
		return (DataPool<R>) getEntities();
	}
	
	private boolean addEntity(E e){
		synchronized(districtEntities){
			return currentEntities.add(e);
		}
	}
	
	private boolean removeEntity(E e){
		synchronized(districtEntities){
			return currentEntities.remove(e);
		}
	}
*/
	/*
	@Override
	synchronized Subject<E, ?> freeSubject(int i){
		Subject<E, ?> res = super.freeSubject(i);
		if(res == null){
			return null;
		}
//		removeEntity(res.getEntity());
		return res;
	}
	
	@Override
	int addSubject(Subject<E, ?> subject){
		for(int i=0; i<pool.length; i++){
			if(pool[i].setSubject(subject)){
//				addEntity(subject.getEntity());
				return i;
			}
		}
		//TODO: Need handler for Pool is full exception
		throw new RuntimeException("Pool is full, need handler");
//		throw new PoolFullException(this);
	}
*/
	public boolean hasBoundaries(){
		return boundaries != null;
	}
	
	public Boundary getBoundary(int i){
		if(boundaries == null || i >= boundaries.size() || i < 0){
			return null;
		}
		return boundaries.get(i);
	}

	public void refreshBoundaries(){
		boundaries = null;
		ArrayList<Boundary> newBoundaries = new ArrayList<Boundary>(4);
		District one, two, three, fore;
		Ipoint corner = getCorner();
		Ipoint dimm = getDimentions();
		one = world.getDistrict(rowNumber, colNumber + 1);
		if(one == null){
			newBoundaries.add(new Boundary(this, 1, Boundary.SIDE_BORDER
					, new Ipoint(corner.getX() + dimm.getX(), corner.getY() - luft)
					, new Ipoint(corner.getX() + dimm.getX(), corner.getY() + dimm.getY() + luft)
			));
		}
		two = world.getDistrict(rowNumber + 1, colNumber);
		if(two == null){
			newBoundaries.add(new Boundary(this, 2, Boundary.SIDE_BORDER
					, new Ipoint(corner.getX() + dimm.getX() + luft, corner.getY() + dimm.getY())
					, new Ipoint(corner.getX() - luft, corner.getY() + dimm.getY())
			));
		}
		three = world.getDistrict(rowNumber, colNumber - 1);
		if(three == null){
			newBoundaries.add(new Boundary(this, 3, Boundary.SIDE_BORDER
					, new Ipoint(corner.getX(), corner.getY() + dimm.getY() + luft)
					, new Ipoint(corner.getX(), corner.getY() - luft)
			));
		}
		fore = world.getDistrict(rowNumber - 1, colNumber);
		if(fore == null){
			newBoundaries.add(new Boundary(this, 4, Boundary.SIDE_BORDER
					, new Ipoint(corner.getX() - luft, corner.getY())
					, new Ipoint(corner.getX() + dimm.getX() + luft, corner.getY())
			));
		}
		if(one != null && two != null){
			District cornerD = world.getDistrict(rowNumber + 1, colNumber + 1);
			if(cornerD == null){
				newBoundaries.add(new Boundary(this, 1, Boundary.CORNER_BORDER
						, new Ipoint(corner.getX() + dimm.getX(), corner.getY() + dimm.getY())
						, new Ipoint(corner.getX() + 2*dimm.getX(), corner.getY() + 2*dimm.getY())
				));
			}
		}
		if(two != null && three != null){
			District cornerD = world.getDistrict(rowNumber + 1, colNumber - 1);
			if(cornerD == null){
				newBoundaries.add(new Boundary(this, 2, Boundary.CORNER_BORDER
						, new Ipoint(corner.getX(), corner.getY() + dimm.getY())
						, new Ipoint(corner.getX() - dimm.getX(), corner.getY() + 2*dimm.getY())
				));
			}
		}
		if(three != null && fore != null){
			District cornerD = world.getDistrict(rowNumber - 1, colNumber - 1);
			if(cornerD == null){
				newBoundaries.add(new Boundary(this, 3, Boundary.CORNER_BORDER
						, new Ipoint(corner.getX(), corner.getY())
						, new Ipoint(corner.getX() - dimm.getX(), corner.getY() - dimm.getY())
				));
			}
		}
		if(fore != null && one != null){
			District cornerD = world.getDistrict(rowNumber - 1, colNumber + 1);
			if(cornerD == null){
				newBoundaries.add(new Boundary(this, 4, Boundary.CORNER_BORDER
						, new Ipoint(corner.getX() + dimm.getX(), corner.getY())
						, new Ipoint(corner.getX() + 2*dimm.getX(), corner.getY() - dimm.getY())
				));
			}
		}
		if(!newBoundaries.isEmpty()){
			boundaries = newBoundaries;
		}
		
	}

	public String toString(){
		return String.format("District row=%s,  col=%s, corner=%s", rowNumber, colNumber, getCorner());
	}
	

	public EntityIterator iterator(){
		EntityIterator iterator = localIterator.get();
		iterator.reject();
		return iterator;
	}
	
	private class EntityIterator implements Iterator<R>{
		
		private int cursor = 0;

		public boolean hasNext() {
			return cursor < pool.length;
		}

		public R next() {
			Subject<E, ?, ?> sbj =  pool[cursor++].getSubject();
			if(sbj == null){
				return null;
			}
			return (R) sbj.getEntity();
		}

		public void remove() {
			throw new RuntimeException("District Iterator has no rights to remove data");
		}
		
		public void reject(){
			cursor = 0;
		}
		
	}

}
