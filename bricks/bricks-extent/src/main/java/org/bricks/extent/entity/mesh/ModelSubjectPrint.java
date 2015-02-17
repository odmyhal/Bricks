package org.bricks.extent.entity.mesh;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.PrintStore;
import org.bricks.engine.neve.SubjectPrint;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelSubjectPrint<P extends ModelSubject, EP extends EntityPrint> extends SubjectPrint<P, EP> {
	
	protected final Matrix4 transform = new Matrix4();
	protected final Map<Node, NodeDataPrint> nodeData = new HashMap<Node, NodeDataPrint>();
	protected boolean edit;

	public ModelSubjectPrint(PrintStore<P, ?> ps) {
		super(ps);
	}

	protected void init(){
		super.init();
		ModelSubject target = getTarget();
		Map<Node, NodeData> nm = target.dataNodes;
		for(Entry<Node, NodeData> entry : nm.entrySet()){
			nodeData.put(entry.getKey(), entry.getValue().getSafePrint());
		}
		this.edit = target.edit;
		if(this.edit){
			transform.set(target.transform);
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
