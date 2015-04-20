package org.bricks.extent.subject.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.staff.Entity;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.tool.Logger;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.g3d.model.Node;

public class MBOPrint<P extends ModelBrickOperable<?>> extends MBPrint<P>{

	protected final Map<Node, NodeDataPrint> nodeData = new HashMap<Node, NodeDataPrint>();
	
	public MBOPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

//	@Override
	public void init(){
		super.init();
		ModelBrickOperable target = getTarget();
		Map<Node, NodeData<NodeDataPrint>> nm = target.dataNodes;
		if(nm == null){
			throw new RuntimeException("Oleh, Problem is here....");
		}
		for(Entry<Node, NodeData<NodeDataPrint>> entry : nm.entrySet()){
			nodeData.put(entry.getKey(), entry.getValue().getSafePrint());
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
