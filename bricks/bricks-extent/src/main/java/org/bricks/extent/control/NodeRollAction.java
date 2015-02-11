package org.bricks.extent.control;

import java.util.Arrays;
import java.util.List;

import org.bricks.engine.help.RotationHelper;
import org.bricks.engine.staff.Entity;
import org.bricks.enterprise.control.widget.tool.FlowMutableAction;
import org.bricks.enterprise.control.widget.tool.FlowTouchPad;
import org.bricks.enterprise.d3.help.AlgebraUtils;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author oleh
 *	Just rolling mesh node in render thread
 *	Doesn't modify related subject event if it exists...
 * @param <T>
 * @param <W>
 */
public class NodeRollAction<T extends Entity, W extends FlowTouchPad> extends FlowMutableAction<T, W>{

	private Vector3 rotationSpin = new Vector3();
	private Vector3 rotationPoint = new Vector3();
	private Vector2 touchPercentile = new Vector2();
	private float rotation = 0f, targetRotation = 0f, rotationSpeed = 0f, curRotationSpeed = 0f;;
	private Matrix4 helpMatrix1 = new Matrix4(), helpMatrix2 = new Matrix4();
	private List<Node> nodes;
	private float deltaPotencial = 0f;
	
	private static final float minRotation = (float) Math.PI / 180;
	
	public NodeRollAction(T target, Vector3 spin, Vector3 point, float startRotation, float rotationSpeed, Node... nodes) {
		super(target);
		this.nodes = Arrays.asList(nodes);
		Validate.isTrue(rotationSpeed > 0, "Rotation Speed should be bigger than zero.");
		this.rotationSpeed = rotationSpeed;
		initRotation(spin, point, startRotation);
	}

	public void initRotation(Vector3 spin, Vector3 point, float startRotation){
		rotationSpin.set(spin);
		rotationPoint.set(point);
		rotation = startRotation;
	}

	@Override
	public void init(W widget) {
		touchPercentile.set(widget.getKnobPercentX(), widget.getKnobPercentY());
		touchPercentile.nor();
		while(rotation < 0){
			rotation += RotationHelper.rotationCycle;
		}
		while(rotation >= RotationHelper.rotationCycle){
			rotation -= RotationHelper.rotationCycle;
		}
		float tarRad = (float)AlgebraUtils.trigToRadians(touchPercentile.x, touchPercentile.y);
		RotationHelper.calculateRotationData(rotation, tarRad, rotationSpeed);
		curRotationSpeed = RotationHelper.getCalculatedRotationSpeed();
		targetRotation = RotationHelper.getCalculatedTargetRotation();
		deltaPotencial = 0f;
		System.out.println(this.getClass().getCanonicalName() + " : initialized widget curRotation: "
				+ rotation + ", targetRotation " + targetRotation + ", speed " + curRotationSpeed);
	}

	@Override
	public boolean act(float delta) {
		boolean result = false;
		deltaPotencial += delta;
		float rotationDiff = deltaPotencial * curRotationSpeed;
		if(Math.abs(rotationDiff) > minRotation){

//			System.out.println(this.getClass().getCanonicalName() + " rotation goes");
			rotation += rotationDiff;
			if(curRotationSpeed >= 0){
				if(rotation >= targetRotation){
					rotation = targetRotation;
					result = true;
					rotationDiff -= rotation - targetRotation;
				}
			}else{
				if(rotation <= targetRotation){
					rotation = targetRotation;
					result = true;
					rotationDiff += targetRotation - rotation;
				}
			}
			deltaPotencial = 0f;
			tuneMatrix(rotationDiff);
		}
		return result;
	}

	private void tuneMatrix(float rotationDiff){
		helpMatrix2.idt().setToRotationRad(rotationSpin, rotationDiff).tra();
		for(Node node : nodes){
			//Looks like do not need synchronization...
			node.globalTransform.trn(-rotationPoint.x, -rotationPoint.y, -rotationPoint.z);
			node.globalTransform.mulLeft(helpMatrix2);
			node.globalTransform.trn(rotationPoint);
		}
	}
}
