package org.bricks.enterprise.control.widget.tool;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/** Used for actions wich depends on current rotation of target object,
 * which may be rotated by other systems
 * example CameraMoveAction, CameraRollAction
 * @author oleh
 *
 * @param <T>
 * @param <W>
 */
public abstract class RotationDependAction<T, W extends Widget> extends FlowMutableAction<T, W> {
	
	private static float halfPI = (float) Math.PI / 2;
	private RotationProvider rotationProvider;

	public RotationDependAction(T target, RotationProvider rotationProvider) {
		super(target);
		this.rotationProvider = rotationProvider;
	}
	
	public RotationDependAction(T target, float constantRotation){
		this(target, new StaticRotationProvider(constantRotation));
	}

	protected float getRotation(){
		return rotationProvider.provideRotation() - halfPI;
	}
	
	public interface RotationProvider{
		public float provideRotation();
	}
	
	private static final class StaticRotationProvider implements RotationProvider{
		
		private float rotation;
		
		private StaticRotationProvider(float rotation){
			this.rotation = rotation;
		}

		public float provideRotation() {
			return rotation;
		}
		
	}
}
