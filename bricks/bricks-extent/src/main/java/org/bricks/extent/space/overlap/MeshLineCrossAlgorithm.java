package org.bricks.extent.space.overlap;

import org.bricks.engine.event.overlap.BaseOverlapAlgorithm;
import org.bricks.engine.event.overlap.OverlapAlgorithm;
import org.bricks.engine.help.GausHelper;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.neve.OriginMovePrint;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.staff.Subject;
import org.bricks.extent.subject.model.MBPrint;

import com.badlogic.gdx.math.Vector3;

public abstract class MeshLineCrossAlgorithm<T extends Imprint<? extends Subject>, K extends Imprint<? extends Subject>> 
	extends BaseOverlapAlgorithm<T, K, Vector3> {
	
	protected Vector3 findCrossPoint(MBPrint<?> mbPrint, OriginMovePrint<?, Vector3> omPrint){
		for(SkeletonPrint<?> sp : mbPrint.skeletons){
			float k = skeletonCrossLineK(sp, omPrint);
			if(k == Float.NEGATIVE_INFINITY){
				continue;
			}
			Vector3 lineOrigin = omPrint.getOrigin().source;
			Vector3 lineMove = omPrint.lastMove.source;
			return new Vector3(lineOrigin.x - k * lineMove.x, lineOrigin.y - k * lineMove.y, lineOrigin.z - k * lineMove.z);
		}
		return null;
	}
	
	protected boolean isLineCross(MBPrint<?> mbPrint, OriginMovePrint<?, Vector3> omPrint){
		for(SkeletonPrint<?> sp : mbPrint.skeletons){
			float k = skeletonCrossLineK(sp, omPrint);
			if(k == Float.NEGATIVE_INFINITY){
				continue;
			}
			return true;
		}
		return false;
	}
	
	private float skeletonCrossLineK(SkeletonPrint<?> skPrint, OriginMovePrint<?, Vector3> omPrint){
		Vector3 lineOrigin = omPrint.getOrigin().source;
		float minX, maxX, minY, maxY, minZ, maxZ;
		if(omPrint.lastMove.source.x > 0){
			maxX = lineOrigin.x;
			minX = maxX - omPrint.lastMove.source.x;
		}else{
			minX = lineOrigin.x;
			maxX = minX - omPrint.lastMove.source.x;
		}
		if(omPrint.lastMove.source.y > 0){
			maxY = lineOrigin.y;
			minY = maxY - omPrint.lastMove.source.y;
		}else{
			minY = lineOrigin.y;
			maxY = minY - omPrint.lastMove.source.y;
		}
		if(omPrint.lastMove.source.z > 0){
			maxZ = lineOrigin.z;
			minZ = maxZ - omPrint.lastMove.source.z;
		}else{
			minZ = lineOrigin.z;
			maxZ = minZ - omPrint.lastMove.source.z;
		}
		if(dimentionsNoCross(skPrint.dimentions, minX, minY, minZ, maxX, maxY, maxZ)){
			return Float.NEGATIVE_INFINITY;
		}
		return lineCrosSkeleton(skPrint, lineOrigin, omPrint.lastMove.source);
/*		for(int i = 0; i < skPrint.triangles.length; i++){
			float k = lineCrossTriangle(skPrint.triangles[i], lineOrigin, omPrint.lastMove.source);
			if(k == Float.NEGATIVE_INFINITY){
				continue;
			}
			return k;
		}
		return Float.NEGATIVE_INFINITY;*/
	}
	
	private float lineCrosSkeleton(SkeletonPrint<?> skPrint, Vector3 lineOrigin, Vector3 lastMove){
		for(int i = 0; i < skPrint.triangles.length; i++){
			float k = lineCrossTriangle(skPrint.triangles[i], lineOrigin, lastMove);
			if(k == Float.NEGATIVE_INFINITY){
				continue;
			}
			return k;
		}
		return Float.NEGATIVE_INFINITY;
	}
	
	public void lineCrosSkeletonAll(MBPrint<?> mbPrint, Vector3 lineOrigin, Vector3 lastMove, float[] dest){
		int index = 0;
		for(SkeletonPrint<?> skPrint : mbPrint.skeletons){
			for(int i = 0; i < skPrint.triangles.length; i++){
				float k = lineCrossTriangle(skPrint.triangles[i], lineOrigin, lastMove);
				if(k == Float.NEGATIVE_INFINITY){
					continue;
				}
				dest[index++] = k;
				if(dest.length == index){
					return;
				}
			}
		}
	}
	
	private boolean dimentionsNoCross(Dimentions3D dimm, float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		if(dimm.max.x < minX || dimm.min.x > maxX
				|| dimm.max.y < minY || dimm.min.y > maxY
				|| dimm.max.z < minZ || dimm.min.z > maxZ){
			return true;
		}
		return false;
	}
	
	private float lineCrossTriangle(Triangle triangle, Vector3 lineOrigin, Vector3 lineMove){
		float BAX = triangle.B.x - triangle.A.x;
		float BAY = triangle.B.y - triangle.A.y; 
		float BAZ = triangle.B.z - triangle.A.z;
		float CAX = triangle.C.x - triangle.A.x;
		float CAY = triangle.C.y - triangle.A.y;
		float CAZ = triangle.C.z - triangle.A.z;
		float k = GausHelper.resolveFirstOf3(lineMove.x, BAX, CAX, triangle.A.x - lineOrigin.x, 
											lineMove.y, BAY, CAY, triangle.A.y - lineOrigin.y,
											lineMove.z, BAZ, CAZ, triangle.A.z - lineOrigin.z);
		if( k < 0 || k > 1 ){
			return Float.NEGATIVE_INFINITY;
		}
		float b;
		if(BAX == 0){
			b = GausHelper.resolveFirstOf2(BAY, CAY, triangle.A.y - lineOrigin.y + lineMove.y * k, 
					BAZ, CAZ, triangle.A.z - lineOrigin.z + lineMove.z * k);
		}else if(BAY == 0){
			b = GausHelper.resolveFirstOf2(BAX, CAX, triangle.A.x - lineOrigin.x + lineMove.x * k, 
					BAZ, CAZ, triangle.A.z - lineOrigin.z + lineMove.z * k);
		}else{
			b = GausHelper.resolveFirstOf2(BAX, CAX, triangle.A.x - lineOrigin.x + lineMove.x * k, 
					BAY, CAY, triangle.A.y - lineOrigin.y + lineMove.y * k);
		}
		if( b < 0 || b > 1 ){
			return Float.NEGATIVE_INFINITY;
		}
		float c;
		if(CAX != 0){
			c = (lineOrigin.x - triangle.A.x - lineMove.x * k - BAX * b) / CAX;
		}else if(CAY != 0){
			c = (lineOrigin.y - triangle.A.y - lineMove.y * k - BAY * b) / CAY;
		}else if(CAZ != 0){
			c = (lineOrigin.z - triangle.A.z - lineMove.z * k - BAZ * b) / CAZ;
		}else{
			return Float.NEGATIVE_INFINITY;
		}
		if( c < 0 || c > 1 || c + b > 1 ){
			return Float.NEGATIVE_INFINITY;
		}
		return k;
	}
	
	
}
