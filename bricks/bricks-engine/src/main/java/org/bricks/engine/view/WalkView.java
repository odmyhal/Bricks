package org.bricks.engine.view;

import org.bricks.core.entity.Fpoint;
import org.bricks.engine.staff.Walker;

public class WalkView<W extends Walker> extends RollView<W>{
	
	private final Fpoint vector = new Fpoint(0f, 0f);

	public WalkView(W entity) {
		super(entity);
	}
	
	public Fpoint getVector(){
		return vector;
	}
	
	@Override
	public void init(){
		super.init();
		Fpoint eVector = this.entity.getVector();
		this.vector.setX(eVector.getFX());
		this.vector.setY(eVector.getFY());
	}

}
