package org.bricks.engine.processor;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.CheckerType;
import org.bricks.engine.processor.Processor;
import org.bricks.engine.processor.tool.Approver;
import org.bricks.engine.staff.Liver;

public abstract class ApproveProcessor<T extends Liver> extends Processor<T> {
	
	private final Collection<Approver<T>> approvers = new LinkedList<Approver<T>>();
	
	public ApproveProcessor(CheckerType checkerType, Approver<T>... approvers){
		super(checkerType);
		this.approvers.addAll(Arrays.asList(approvers));
	}
	
	@Override
	protected Event popEvent(T target, long eventTime) {
		boolean approved = true;
		for(Approver<T> approver : approvers){
			approved &= approver.approve(target, eventTime);
		}
		if(approved){
			process(target, eventTime);
		}
		return null;
	}

	protected boolean addApprover(Approver<T> approver){
		return approvers.add(approver);
	}
}
