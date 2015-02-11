package org.bricks.extent.entity.mesh;

import java.util.LinkedList;

import org.bricks.engine.view.DurableView;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class NodeDataView extends DurableView{
	
	private NodeData nodeData;
	public final Quaternion rotation = new Quaternion();
	public final Vector3 translation = new Vector3();

	public NodeDataView(NodeData nd) {
		super(nd.viewCache());
		this.nodeData = nd;
	}

	public void init(){
		this.rotation.set(nodeData.rotation);
		this.translation.set(nodeData.translation);
	}
}
