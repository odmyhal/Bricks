package org.bricks.engine.event.overlap;

import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.EntityCore;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.staff.Subject;

public abstract class OverlapStrategy<T extends Liver, H extends Habitant, TD, HD, P> {

	private OverlapAlgorithm<TD, HD, P> algorithm;
	private HabitantDataExtractor<H, HD> extractor;
	private EventProducer<TD, HD, P> producer;
	
	public OverlapStrategy(OverlapAlgorithm<TD, HD, P> algorithm, 
			HabitantDataExtractor<H, HD> extractor, EventProducer<TD, HD, P> producer){
		this.algorithm = algorithm;
		this.extractor = extractor;
		this.producer = producer;
	}
	
	public abstract boolean hasToCheckOverlap(T target, H source);
	
	public OverlapEvent<TD, HD, P, ? extends EntityCore> checkForOverlapEvent(TD targetData, H habitant){
		HD clientData = extractor.extractHabitantData(habitant);
		P touchPoint = algorithm.findOverlapPoint(targetData, clientData);
		if(touchPoint == null){
			extractor.freeHabitantData(clientData);
			return null;
		}
		return producer.produceEvent(targetData, clientData, touchPoint);
	}
	
	public OverlapEvent<HD, TD, P, ? extends EntityCore> produceRevertEvent(OverlapEvent<TD, HD, P, ? extends EntityCore> event){
		return producer.produceRevertEvent(event.getSourceData(), event.getTargetData(), event.getTouchPoint());
	}
	
	public boolean isOverlap(TD targetData, H habitant){
		HD clientData = extractor.extractHabitantData(habitant);
		return algorithm.isOvarlap(targetData, clientData);
	}
	
/*	public PrintOverlapEvent checkForOverlapEvent(Imprint targetSubjectPrint, Imprint clientSubjectPrint){
		return algorithm.checkOverlap(targetSubjectPrint, clientSubjectPrint);
	}
	
	public boolean isOverlap(Imprint targetSubjectPrint, Imprint clientSubjectPrint){
		return algorithm.isOvarlap(targetSubjectPrint, clientSubjectPrint);
	}
*/	
	public interface HabitantDataExtractor<HT1 extends Habitant, HD1>{
		public HD1 extractHabitantData(HT1 habitant);
		public void freeHabitantData(HD1 clientData);
	}
	
	public interface EventProducer<TD2, HD2, P2>{
		public OverlapEvent<TD2, HD2, P2, ? extends EntityCore> produceEvent(TD2 targetData, HD2 clientData, P2 touchPoint);
		public OverlapEvent<HD2, TD2, P2, ? extends EntityCore> produceRevertEvent(HD2 targetData, TD2 clientData, P2 touchPoint);
	}
	
	public static class TrueOverlapStrategy extends OverlapStrategy{

		public TrueOverlapStrategy(OverlapAlgorithm oa, HabitantDataExtractor extractor, EventProducer producer) {
			super(oa, extractor, producer);
		}

		@Override
		public boolean hasToCheckOverlap(Liver target, Habitant source) {
			return true;
		}
		
	}
}
