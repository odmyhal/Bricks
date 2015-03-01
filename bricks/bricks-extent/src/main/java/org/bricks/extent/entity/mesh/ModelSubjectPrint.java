package org.bricks.extent.entity.mesh;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.SubjectPrint;
import org.bricks.exception.Validate;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelSubjectPrint<P extends ModelSubjectOperable, EP extends EntityPrint> extends SubjectPrint<P, EP> {
	
	protected final Matrix4 transform = new Matrix4();
	protected final Map<Node, NodeDataPrint> nodeData = new HashMap<Node, NodeDataPrint>();
	//should be less than target.lastPrintModified(-5)
	protected int lastPrintModified = -6;

	public ModelSubjectPrint(PrintStore<P, ?> ps) {
		super(ps);
	}
	
	@Override
	protected void init(){
		super.init();
		ModelSubjectOperable target = getTarget();
		Map<Node, NodeData<NodeDataPrint>> nm = target.dataNodes;
		for(Entry<Node, NodeData<NodeDataPrint>> entry : nm.entrySet()){
			nodeData.put(entry.getKey(), entry.getValue().getSafePrint());
		}
//		this.lastPrintModified = target.lastPrintModified;
//		Validate.isFalse(lastPrintModified > target.currentPrint, "Last modified print can't be bigger than currentPrint");
		if(this.lastPrintModified < target.lastPrintModified){
			transform.set(target.transform);
			this.lastPrintModified = target.lastPrintModified;
//			System.out.println("ModelsubjectPrint inited with matrix: " + transform);
		}
	}
	
	protected void endUse(){
		for(NodeDataPrint ndp : nodeData.values()){
			ndp.free();
		}
		nodeData.clear();
		super.endUse();
	}
}
