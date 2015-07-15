package org.bricks.extent.effects;

import org.bricks.extent.effects.BricksParticleSystem.NonContiniousEmitter;

import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public abstract class TemporaryEffect extends ParticleEffect{
	
	protected final SubChannelRenderer subChannelRenderer;

	public TemporaryEffect(ParticleSystem particleSystem){
		NonContiniousEmitter emitter = provideEmitter();
		subChannelRenderer = provideRenderer();
		ParticleController controller1 = provideController(particleSystem, emitter, (ParticleControllerRenderer) subChannelRenderer);
		Array<ParticleController> controllers = getControllers();
		controllers.add(controller1);
	}
	
	protected void setToTranslation(Vector3 trn){
		Array<ParticleController> controllers = getControllers();
		for (int i = 0, n = controllers.size; i < n; i++){
			ParticleController controller = controllers.get(i);
			controller.transform.idt();
			controller.translate(trn);
		}
	}
	
	public void finish(){}
	
	public void init(){
		super.init();
		this.subChannelRenderer.setIdleController();
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
	
	protected abstract SubChannelRenderer provideRenderer();
	protected abstract NonContiniousEmitter provideEmitter();
	protected abstract ParticleController provideController(ParticleSystem particleSystem, NonContiniousEmitter emitter, ParticleControllerRenderer renderer);
}

