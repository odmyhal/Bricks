package org.bricks.extent.debug;

import org.bricks.core.entity.Ipoint;
import org.bricks.core.entity.Point;
import org.bricks.engine.Engine;
import org.bricks.engine.item.MultiSubjectEntity;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.PlanePointsPrint;
import org.bricks.engine.pool.Boundary;
import org.bricks.engine.pool.District;
import org.bricks.engine.staff.Subject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

public class ShapeDebugger implements Disposable{

	public ShapeRenderer shR = new ShapeRenderer();
	
	
	public <R> void drawSectors(Engine engine, Matrix4 cameraMatrix, Iterable<District> districts){
		shR.setProjectionMatrix(cameraMatrix);
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		for(District<?> d: districts){
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
	
	public <Z> void drawSpaceShapes(ModelBatch modelBatch, Iterable<Z> entities){
		for(Z entity : entities){
			if(entity instanceof SpaceDebug){
				modelBatch.render(((SpaceDebug) entity).debugModel());
			}
		}
	}
	
	public <K> void drawEntityShapes(Iterable<K> entities, Matrix4 cameraMatrix){
		shR.setProjectionMatrix(cameraMatrix);
		shR.begin(ShapeType.Line);
		shR.setColor(Color.RED);
		for(K entity: entities){
			if(entity instanceof MultiSubjectEntity){
				MultiSubjectEntity<Subject, ?, ?> msubjecte = (MultiSubjectEntity<Subject, ?, ?>) entity;
//				System.out.println("Debug draw " + msubjecte.getClass().getCanonicalName());
				for(int i = 0; i < msubjecte.getStaff().size(); i++){
					Imprint print = msubjecte.getStaff().get(i).getSafePrint();
					if(print instanceof PlanePointsPrint){
						drawPoints(((PlanePointsPrint) print).getPoints(), cameraMatrix);
					}
					print.free();
/*					if(sv instanceof BrickSubject){
						drawPoints(((BrickSubject)sv).getBrick().getPoints(), cameraMatrix);
					}else if(sv instanceof SpaceSubject){
						MarkPoint markPoint = ((SpaceSubject) sv).markPoint;
						Vector3 one = markPoint.getMark(1);
						Vector3 two = markPoint.getMark(2);
						shR.line(one.x, one.y, two.x, two.y);
					}*/
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
			for(int i = 0; i < msubjecte.getStaff().size(); i++){
				Imprint print = msubjecte.getStaff().get(i).getSafePrint();
				if(print instanceof PlanePointsPrint){
					drawPoints(((PlanePointsPrint) print).getPoints(), cameraMatrix);
				}
/*				if(sv instanceof BrickSubject){
					drawPoints(((BrickSubject)sv).getBrick().getPoints(), cameraMatrix);
				}*/
				print.free();
			}
		}
		shR.end();
	}
	
	private void drawPoints(Iterable<Point> points, Matrix4 cameraMatrix){
		
		Point one = null, first = null;
		for(Point p: points){
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
