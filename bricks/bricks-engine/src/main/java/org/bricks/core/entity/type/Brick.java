package org.bricks.core.entity.type;

import java.util.Collection;
import java.util.List;

import org.bricks.core.entity.Dimentions;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.exception.BrickInitException;

public interface Brick {
	
//	public void initialize();
//	public void initialize(Point origin);
	public float getWeight();

	public Dimentions getDimentions();
	public void rotate(float rad, Point p);
	public void translate(int x, int y);

	public List<Ipoint> getPoints();
	public Point getCenter();
	public int size();

}
