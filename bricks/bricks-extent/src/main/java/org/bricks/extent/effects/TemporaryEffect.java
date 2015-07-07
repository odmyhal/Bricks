package org.bricks.extent.effects;

import org.bricks.extent.effects.EffectSystem.NonContiniousEmitter;

import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public abstract class TemporaryEffect extends ParticleEffect{

	public TemporaryEffect(ParticleSystem particleSystem){
		NonContiniousEmitter emitter = provideEmitter();
		ParticleController controller1 = provideController(particleSystem, emitter);
//		ParticleController controller2 = controller1.copy();
		Array<ParticleController> controllers = getControllers();
		controllers.add(controller1);
//		controllers.add(controller2);
	}
	
	protected void setToTranslation(Vector3 trn){
		Array<ParticleController> controllers = getControllers();
		for (int i = 0, n = controllers.size; i < n; i++){
			ParticleController controller = controllers.get(i);
			controller.transform.idt();
			controller.translate(trn);
		}
	}
	
	public boolean done(){
		Array<ParticleController> controllers = getControllers();
		for (int i = 0, n = controllers.size; i < n; i++){
			ParticleController controller = controllers.get(i);
			if(controller.particles.size > 0 || !((NonContiniousEmitter)controller.emitter).finished()){
				return false;
			}
		}
		return true;
	}
	
	public void setDeltaTime(float deltaTime){
//		controller.deltaTime = deltaTime;
//		controller.deltaTimeSqr = deltaTime * deltaTime;
	}
	
	protected abstract NonContiniousEmitter provideEmitter();
	protected abstract ParticleController provideController(ParticleSystem particleSystem, NonContiniousEmitter emitter);
}

