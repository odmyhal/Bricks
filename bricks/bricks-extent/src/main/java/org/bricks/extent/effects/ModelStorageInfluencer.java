package org.bricks.extent.effects;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.exception.Validate;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.ObjectChannel;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ModelInfluencer;

public class ModelStorageInfluencer extends ModelInfluencer.Single{
	
	private String modelName;
	
	public ModelStorageInfluencer(String modelName){
		this.modelName = modelName;
	}
	
	private ModelStorageInfluencer(ModelStorageInfluencer origin, String modelName){
		super(origin);
		this.modelName = modelName;
	}
	
	@Override
	public void init () {
		ObjectChannel<ModelInstance> modelChannel = controller.particles.getChannel(ParticleChannels.ModelInstance);
		Validate.isFalse(modelChannel == null, "ModelChanner is not set.");
		for(int i=0, c = controller.emitter.maxParticleCount; i < c; ++i){
			ModelInstance dustInstance = ModelStorage.instance().getModelInstance(modelName);
/*			BlendingAttribute blendingAttribute = (BlendingAttribute)dustInstance.materials.get(0).get(BlendingAttribute.Type);
			if(blendingAttribute == null){
				System.out.println("Here is Problem : no blending attribute found");
			}else{
				System.out.println("ITI S OK water ball has opacity: " + blendingAttribute.opacity);
			}*/
			Validate.isFalse(dustInstance == null);
			modelChannel.data[i] = dustInstance;
		}
	}
	
	@Override
	public ModelStorageInfluencer copy () {
		return new ModelStorageInfluencer(this, modelName);
	}
}
