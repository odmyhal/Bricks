package org.bricks.extent.debug;

import java.util.Collection;

import org.bricks.core.entity.Ipoint;
import org.bricks.engine.Engine;
import org.bricks.engine.item.MultiSubjectEntity;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.pool.District;
import org.bricks.engine.staff.Subject;
import org.bricks.extent.space.MarkPoint;
import org.bricks.extent.space.SpaceSubject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class ShapeDebugger implements Disposable{

	public ShapeRenderer shR = new ShapeRenderer();
	
	
	public <R> void drawSectors(Engine<R> engine, Matrix4 cameraMatrix){
		shR.setProjectionMatrix(cameraMatrix);
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		for(District<R, ?> d: engine.getDistricts()){
//			if(d.hasBoundaries()){
				int n = 0;
				Ipoint corner = d.getCorner();
				shR.line(corner.getFX(), corner.getFY(), corner.getFX() + 250, corner.getFY());
				shR.line(corner.getFX(), corner.getFY(), corner.getFX(), corner.getFY() + 250);
				Boundary b = null;
				while((b = d.getBoundary(n++)) != null){
					if(b.sourceType().equals(Boundary.SIDE_BORDER)){
						Ipoint first = b.getFirst();
						Ipoint second = b.getSecond();
						shR.line(first.getFX(), first.getFY(), second.getFX(), second.getFY());
					}
				}
//			}
			
		}
		shR.end();
		
	}
	
	public <K> void drawEntityShapes(Collection<K> entities, Matrix4 cameraMatrix){
		shR.setProjectionMatrix(cameraMatrix);
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		for(K entity: entities){
			if(entity instanceof MultiSubjectEntity){
				MultiSubjectEntity<Subject, ?, ?> msubjecte = (MultiSubjectEntity<Subject, ?, ?>) entity;
				for(Subject sv : msubjecte.getStaff()){
					if(sv instanceof BrickSubject){
						drawPoints(((BrickSubject)sv).getBrick().getPoints(), cameraMatrix);
					}else if(sv instanceof SpaceSubject){
						MarkPoint markPoint = ((SpaceSubject) sv).markPoint;
						Vector3 one = markPoint.getMark(1);
						Vector3 two = markPoint.getMark(2);
						shR.line(one.x, one.y, two.x, two.y);
					}
				}
			}
		}
		shR.end();
	}
	
	public <K> void drawEntityShape(K entity, Matrix4 cameraMatrix){
		shR.setProjectionMatrix(cameraMatrix);
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		if(entity instanceof MultiSubjectEntity){
			MultiSubjectEntity<Subject, ?, ?> msubjecte = (MultiSubjectEntity<Subject, ?, ?>) entity;
			for(Subject sv : msubjecte.getStaff()){
				if(sv instanceof BrickSubject){
					drawPoints(((BrickSubject)sv).getBrick().getPoints(), cameraMatrix);
				}
			}
		}
		shR.end();
	}
	
	private void drawPoints(Collection<Ipoint> points, Matrix4 cameraMatrix){
		
		Ipoint one = null, first = null;
		for(Ipoint p: points){
			if(one == null){
				one = first = p;
				continue;
			}
			shR.line(p.getFX(), p.getFY(), one.getFX(), one.getFY());
			one = p;
		}
		shR.line(first.getFX(), first.getFY(), one.getFX(), one.getFY());
	}
	
	public void dispose() {
		shR.dispose();
	}

}
