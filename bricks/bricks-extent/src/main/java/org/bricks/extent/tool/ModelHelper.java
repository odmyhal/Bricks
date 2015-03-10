package org.bricks.extent.tool;

import com.badlogic.gdx.graphics.g3d.model.Node;

public class ModelHelper {

	public static void calculateNodeGlobalTransforms(Node node){
		node.calculateWorldTransform();
		for(Node chn : node.children){
			calculateNodeGlobalTransforms(chn);
		}
	}
}
