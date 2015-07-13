package org.bricks.extent.effects;

import org.bricks.exception.NotSupportedMethodException;

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer;

public interface SubChannelRenderer{
	
	public void setIdleController();
	public void setControllerSize(int size);
	
	public void setPositionChannel(FloatChannel positionChannel);
	public void setScaleChannel(FloatChannel scaleChannel);
	public void setRotation2DChannel(FloatChannel rotationChannel);
	public void setRotation3DChannel(FloatChannel rotationChannel);
	public void setColorChannel(FloatChannel colorChannel);
	
	
	public static class SubChannelModelInstanceRenderer 
		extends ModelInstanceRenderer
		implements SubChannelRenderer{
		

		public void setIdleController() {
			ParticleController idleController = new ParticleController();
			idleController.particles = new ParallelArray(0);
			this.renderData.controller = idleController;
		}

		public void setControllerSize(int size) {
			this.renderData.controller.particles.size = size;
		}

		public void setPositionChannel(FloatChannel positionChannel) {
			this.renderData.positionChannel = positionChannel;
		}

		public void setScaleChannel(FloatChannel scaleChannel) {
			this.renderData.scaleChannel = scaleChannel;
		}

		public void setRotation2DChannel(FloatChannel rotationChannel) {
			throw new NotSupportedMethodException("ModelRenderer does not use rotation2DChannel");
		}

		public void setRotation3DChannel(FloatChannel rotationChannel) {
			this.renderData.rotationChannel = rotationChannel;
		}

		public void setColorChannel(FloatChannel colorChannel) {
			this.renderData.colorChannel = colorChannel;
		}

	}
	
}
