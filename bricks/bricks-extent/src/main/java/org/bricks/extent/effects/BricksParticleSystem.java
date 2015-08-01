package org.bricks.extent.effects;

import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;

public class BricksParticleSystem {

	private static final ParticleSystem particleSystem = ParticleSystem.get();
	
	public static void addBatch(ParticleBatch batch){
		particleSystem.add(batch);
	}
	
	public static void begin(){
		particleSystem.begin();
	}
	
	public static void end(){
		particleSystem.end();
	}
	
	public static ParticleSystem particleSystem(){
		return particleSystem;
	}
	
	public static void drawEffects(Iterable<EffectSubject> effects){
		for(EffectSubject effect : effects){
			effect.blockActive();
			effect.draw();
		}
	}
	
	public static void freeEffects(Iterable<EffectSubject> effects){
		for(EffectSubject effect : effects){
			effect.freeActive();
		}
	}

	public static class NonContiniousEmitter extends RegularEmitter{
		
		public NonContiniousEmitter(){
			setContinuous(false);
		}
		
		private NonContiniousEmitter(NonContiniousEmitter origin){
			super(origin);
			setContinuous(false);
		}
		
		public boolean finished(){
			return durationTimer >= duration;
		}
		
		@Override
		public ParticleControllerComponent copy () {
			return new NonContiniousEmitter(this);
		}
/*		
		public String showTimerData(){
			return "durationTimer: " + durationTimer + ", duration: " + duration;
		}
		*/
	}
	
	
}
