package com.odmyha.weapon;

import java.util.Collection;
import java.util.LinkedList;

import org.bircks.entierprise.model.ModelStorage;
import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.impl.PointSetBrick;
import org.bricks.core.entity.type.Brick;
import org.bricks.engine.item.Stone;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.pool.Subject;
import org.bricks.extent.entity.mesh.ModelSubject;
import org.bricks.extent.entity.mesh.ModelSubjectPrint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Shield extends Stone<ModelSubject, EntityPrint> implements RenderableProvider{
	
	public static final String SHIELD_SOURCE = "ShieldSource@shoot.odmyha.com";

	private Shield(ModelSubject subject) {
		super(subject);
	}
	
	public static Shield instance(){
		Collection<Ipoint> points = new LinkedList<Ipoint>();
		points.add(new Ipoint(0, 0));
		points.add(new Ipoint(50, 0));
		points.add(new Ipoint(50, 200));
		points.add(new Ipoint(0, 200));
		for(Ipoint p: points){
			p.setX(p.getX() - 25);
			p.setY(p.getY() - 100);
		}
		Brick brick = new PointSetBrick(points);
/*		ModelInstance mi = createModelInstance();
		mi.materials.get(0).set(ColorAttribute.createDiffuse(Color.GRAY));
		ModelSubject<Shield> ms = new ModelSubject<Shield>(brick, mi);
//		ms.getModelInstance().materials.get(0).set(ColorAttribute.createDiffuse(Color.GRAY));*/
		ModelInstance shield = ModelStorage.instance().getModelInstance("shield");
		ModelSubject<Shield, ModelSubjectPrint> ms = new ModelSubject<Shield, ModelSubjectPrint>(brick, shield);
		return new Shield(ms);
	}
	
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(ModelSubject subject: getStaff()){
			subject.getRenderables(renderables, pool);
		}
	}

	public String sourceType() {
		return SHIELD_SOURCE;
	}

}
