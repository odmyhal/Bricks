package org.bricks.extent.effects;

import org.bricks.utils.ThreadTransferCache;

import com.badlogic.gdx.math.Vector3;

public class EffectTransferData<K extends TemporaryEffect> extends ThreadTransferCache.AbstractTransferData {

	protected Class<K> effectClass;
	protected Vector3 translationData = new Vector3();
	
	public void setClass(Class<K> clazz){
		this.effectClass = clazz;
	}
	
	public void setTranslation(float x, float y, float z){
		translationData.set(x, y, z);
	}
}
