package org.bricks.engine.help;

import org.bricks.exception.Validate;
import org.bricks.core.entity.Fpoint;
import org.bricks.core.entity.Point;
import org.bricks.core.help.PointHelper;
import org.bricks.core.help.VectorHelper;
import org.bricks.engine.view.SubjectView;
import org.bricks.engine.view.WalkView;

public class VectorSwapHelper {

	public static Fpoint fetchSwapVector(SubjectView<?, WalkView> target, SubjectView<?, WalkView> source){
		
		Fpoint targetVector = target.getEntityView().getVector();
		
		Fpoint minusSwap = fetchMinusSwap(target, source);
		Fpoint plusSwap = fetchMinusSwap(source, target);
		//Changes for weight
		float k = source.getWeight() / target.getWeight();
		Fpoint targetPlusSwap = new Fpoint(plusSwap.getFX() * k, plusSwap.getFY() * k);
//changes finish
		float targetNX = targetVector.getFX() + targetPlusSwap.getFX() - minusSwap.getFX();
		float targetNY = targetVector.getFY() + targetPlusSwap.getFY() - minusSwap.getFY();
		
		float x = targetPlusSwap.getFX() - minusSwap.getFX();
		float y = targetPlusSwap.getFY() - minusSwap.getFY();
		if(targetNX != 0 || targetNY != 0){
			Fpoint sourceVector = source.getEntityView().getVector();
			double totalImpuls = VectorHelper.vectorLen(targetVector) * target.getWeight() + VectorHelper.vectorLen(sourceVector) * source.getWeight();
			k = target.getWeight() / source.getWeight();
			Fpoint sourcePlusSwap = new Fpoint(minusSwap.getFX() * k, minusSwap.getFY() * k); 
			float sourceNX = sourceVector.getFX() - plusSwap.getFX() + sourcePlusSwap.getFX();
			float sourceNY = sourceVector.getFY() - plusSwap.getFY() + sourcePlusSwap.getFY();
			Fpoint newTarget = new Fpoint(targetNX, targetNY);
			Fpoint newSource = new Fpoint(sourceNX, sourceNY);
//System.out.println("Approximat new target is " + newTarget);			
			double targetNewImpuls = VectorHelper.vectorLen(newTarget) * target.getWeight();
			double newTotalImpuls = targetNewImpuls + VectorHelper.vectorLen(newSource) * source.getWeight();
			double targetPart = targetNewImpuls / newTotalImpuls;
			double targetImpulsDiff = (totalImpuls - newTotalImpuls) * targetPart;
			double targetLenDiff = targetImpulsDiff / target.getWeight();
			
			newTarget = PointHelper.normalize(newTarget, Math.abs(targetLenDiff));
			if(targetLenDiff < 0){
				newTarget.setX(newTarget.getFX() * (-1));
				newTarget.setY(newTarget.getFY() * (-1));
			}
			x += newTarget.getFX();
			y += newTarget.getFY();
		}
		return new Fpoint(x, y);
	}
	
	//public just for test
	private static Fpoint fetchMinusSwap(SubjectView<?, WalkView> target, SubjectView<?, WalkView> source){
		Point tCenter = target.getCenter();
		Point sCenter = source.getCenter();
		Fpoint hitVector = new Fpoint(sCenter.getFX() - tCenter.getFX(), sCenter.getFY() - tCenter.getFY());
		if(hitVector.getFX() == 0 && hitVector.getFY() == 0){
			return hitVector;
		}
		Fpoint myVector = target.getEntityView().getVector();
		
		Fpoint swap = VectorHelper.vectorProjection(myVector, hitVector);
//		double k = 1;//for test
		double k = Math.min(1, source.getWeight() / target.getWeight() );
		if(swap.getFX() * hitVector.getFX() < 0){
			swap.setX(0);
		}else{
			swap.setX((float) (swap.getFX() * k));
		}
		if(swap.getFY() * hitVector.getFY() < 0){
			swap.setY(0);
		}else{
			swap.setY((float) (swap.getFY() * k));
		}

//		return swap;
		
		Fpoint hisWay = VectorHelper.vectorProjection(source.getEntityView().getVector(), hitVector);
		double p = Math.min(1, source.getWeight() / target.getWeight());
		if(hisWay.getFX() * hitVector.getFX() < 0){
			hisWay.setX(0);
		}else{
			hisWay.setX((float)(hisWay.getFX() * p));
		}
		if(hisWay.getFY() * hitVector.getFY() < 0){
			hisWay.setY(0);
		}else{
			hisWay.setY((float)(hisWay.getFY() * p));
		}
		Validate.isTrue(hisWay.getFX() * swap.getFX() >= 0);
		Validate.isTrue(hisWay.getFY() * swap.getFY() >= 0);
		float nSwapX =  Math.abs(swap.getFX()) > Math.abs(hisWay.getFX()) ? swap.getFX() - hisWay.getFX() : 0;
		float nSwapY =  Math.abs(swap.getFY()) > Math.abs(hisWay.getFY()) ? swap.getFY() - hisWay.getFY() : 0;
		swap.setX(nSwapX);
		swap.setY(nSwapY);
		return swap;
	}
	
	public static Fpoint fetchReturnVector(SubjectView<?, WalkView> target, Point touch){
		Point tCenter = target.getCenter();
		Fpoint hitVector = new Fpoint(touch.getFX() - tCenter.getFX(), touch.getFY() - tCenter.getFY());
		Fpoint myVector = target.getEntityView().getVector();
		Fpoint ret = VectorHelper.vectorProjection(myVector, hitVector);
		if(ret.getFX() * hitVector.getFX() < 0){
			ret.setX(0);
		}else{
			ret.setX(-2 * ret.getFX());
		}
		if(ret.getFY() * hitVector.getFY() < 0){
			ret.setY(0);
		}else{
			ret.setY(-2 * ret.getFY());
		}
		return ret;
	}
	
	public static Point getRollVector(Point touch, Point center, float rotationSpeed){
		if(rotationSpeed == 0){
			return new Fpoint(0f, 0f);
		}
		Fpoint nVector = new Fpoint(touch.getFX() - center.getFX(), touch.getFY() - center.getFY());
		double nLen = VectorHelper.vectorLen(nVector);
		if(nLen == 0){
			return new Fpoint(0f, 0f);
		}
		double cos = 0;
		double sin = 1;
		if(rotationSpeed < 0){
			sin = -1;
			rotationSpeed *= -1;
		}
		PointHelper.rotatePointByZero(nVector, sin, cos, nVector);
		return PointHelper.normalize(nVector, nLen * rotationSpeed);
	}
}
