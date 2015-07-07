package org.bricks.extent.effects;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.exception.Validate;
import org.bricks.utils.Cache;
import org.bricks.utils.Cache.DataProvider;
import org.bricks.utils.LinkLoop;
import org.bricks.utils.Loop;
import org.bricks.utils.Quarantine;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.ObjectChannel;
import com.badlogic.gdx.graphics.g3d.particles.batches.ModelInstanceParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ModelInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer;
import com.badlogic.gdx.math.Vector3;

public class EffectSystem {
	
	private static final EffectSystem instance = new EffectSystem();
	public static final AtomicInteger etdInstances = new AtomicInteger();
	
	static{
		Cache.registerTransferCache(EffectTransferData.class, new DataProvider<EffectTransferData>(){
			public EffectTransferData provideNew() {
				etdInstances.incrementAndGet();
				return new EffectTransferData();
			}
		});
	}
	
	private ParticleSystem particleSystem;
//	private ModelInstanceParticleBatch modelBatch;
	private Quarantine<EffectTransferData> buffer = new Quarantine<EffectTransferData>(10);
	private Loop<TemporaryEffect> effects = new LinkLoop<TemporaryEffect>();
	
	public static void update(float deltaTime){
		instance.systemUpdate(deltaTime);
	}
	
	public static ParticleSystem particleSystem(){
		return instance.particleSystem;
	}
	
	/**
	 * Create to be called from motor threads
	 */
	public static void addEffect(Class<? extends TemporaryEffect> effectClass, float x, float y, float z){
		EffectTransferData etd = Cache.get(EffectTransferData.class);
		etd.setClass(effectClass);
		etd.setTranslation(x, y, z);
		instance.buffer.push(etd);
	}
	
	private EffectSystem(){
		particleSystem = ParticleSystem.get();
		ModelInstanceParticleBatch modelBatch = new ModelInstanceParticleBatch();
		particleSystem.add(modelBatch);
	}
	
	private void systemUpdate(float deltaTime){
		for(EffectTransferData<? extends TemporaryEffect> transferData : buffer){
			TemporaryEffect effect = Cache.get(transferData.effectClass);
			effect.init();
			effect.setToTranslation(transferData.translationData);
			particleSystem.add(effect);
			effects.add(effect);
			effect.start();
			Cache.put(transferData);
		}
		Iterator<TemporaryEffect> iterator = effects.iterator();
		while(iterator.hasNext()){
			TemporaryEffect effect = iterator.next();
			if(effect.done()){
				effect.end();
				iterator.remove();
				particleSystem.remove(effect);
				Cache.put(effect);
			}else{
				effect.setDeltaTime(deltaTime);
			}
		}
		particleSystem.update();
		particleSystem.begin();
		particleSystem.draw();
		particleSystem.end();
	}

	public static class NonContiniousEmitter extends RegularEmitter{
		
		public NonContiniousEmitter(){
			setContinuous(false);
		}
		
		public boolean finished(){
			return durationTimer > duration;
		}
	}

}
