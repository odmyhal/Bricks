package org.bricks.extent.space.overlap;

import org.bricks.engine.neve.ContainsEntityPrint;
import org.bricks.engine.neve.OriginMovePrint;
import org.bricks.engine.staff.Subject;
import org.bricks.extent.subject.model.ContainsMBPrint;
import org.bricks.extent.subject.model.MBPrint;
import org.bricks.extent.subject.model.ModelBrickSubject;

import com.badlogic.gdx.math.Vector3;

public class LineCrossMBAlgorithm 
	<T extends ContainsEntityPrint<? extends Subject, ? extends OriginMovePrint<?, Vector3>>, K extends ContainsMBPrint<? extends ModelBrickSubject, ? extends MBPrint>> 
	extends MeshLineCrossAlgorithm<T, K>{

	public Vector3 findOverlapPoint(T target, K client) {
		return this.findCrossPoint(client.linkModelBrickPrint(), target.linkEntityPrint());
	}

	public boolean isOvarlap(T target, K client) {
		return this.isLineCross(client.linkModelBrickPrint(), target.linkEntityPrint());
	}

}
