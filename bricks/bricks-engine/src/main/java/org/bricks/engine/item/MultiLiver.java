package org.bricks.engine.item;

import java.util.Collection;
import java.util.Map;

import org.bricks.core.entity.Point;
import org.bricks.exception.Validate;
import org.bricks.engine.Engine;
import org.bricks.engine.Motor;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.EventTarget;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.event.check.OverlapChecker;
import org.bricks.engine.event.overlap.OSRegister;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.neve.EntityPointsPrint;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.pool.BaseSubject;
import org.bricks.engine.pool.BrickSubject;
import org.bricks.engine.pool.District;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.tool.Live;
import org.bricks.engine.tool.Logger;

public abstract class MultiLiver<S extends Subject, P extends EntityPrint, C> extends MultiSubjectEntity<S, P, C> implements Liver<P>{
	
	private Live live;
	private Logger logger = new Logger();
	private Map<String, OverlapStrategy> overlapStrategy;
	private Motor motor;
	private boolean alive = false;

	protected MultiLiver() {
		overlapStrategy = initOverlapStrategy();
		live = new Live(this);
	}
	
	public final Map<String, OverlapStrategy> initOverlapStrategy(){
		return OSRegister.getClassStrategies(this.getClass());
	}

	public void registerEventChecker(EventChecker checker){
		live.registerEventChecker(checker);
	}
	public boolean unregisterEventChecker(EventChecker checker){
		return live.unregisterEventChecker(checker);
	}
	public boolean addEvent(Event e){
		return live.addEvent(e);
	}
	public boolean checkerRegistered(EventChecker checker){
		return live.checkerRegistered(checker);
	}
	public void refreshCheckers(long currentTime){
		live.refreshCheckers(currentTime);
	}
	public boolean hasChekers(){
		return live.hasChekers();
	}
	public Collection<EventChecker> getCheckers(){
		return live.getCheckers();
	}

	public boolean isEventTarget(){
		return Boolean.TRUE;
	}

	public Event popEvent() {
		return live.popEvent();
	}
	
	public Event putHistory(Event event){
		return live.putHistory(event);
	}
	
	public Event getHistory(Event event){
		return live.getHistory(event);
	}
	
	public Event getHistory(int eventGroupCode){
		return live.getHistory(eventGroupCode);
	}
	
	public Event removeHistory(int groupCode){
		return live.removeHistory(groupCode);
	}
	
	public void motorProcess(long currentTime){
//		System.out.println(this.getClass().getCanonicalName() + " motor process");
		processEvents(currentTime);
	}
	
	public void processEvents(long currentTime){
		refreshCheckers(currentTime);
		if(hasChekers()){
			for(EventChecker checker : getCheckers()){
				if(checker.isActive()){
					checker.checkEvents(this, currentTime);
				}
			}
		}
	}
	
	public OverlapEvent checkOverlap(Subject mySubject, Subject client){
		OverlapStrategy os = overlapStrategy.get(client.getEntity().sourceType());
		if(os == null){
			return null;
		}
		if(os.hasToCheckOverlap(this, client)){
			Validate.isTrue(mySubject.getEntity() == this, "Alloved to check only inner subjects");
			//Has to be synchronized with client popEvetn
			synchronized(client.getEntity()){
				Imprint targetPrint = mySubject.getInnerPrint();
				Imprint checkPrint = client.getSafePrint();
				OverlapEvent oe = os.checkForOverlapEvent(targetPrint, checkPrint);
				if(oe != null){
					targetPrint.occupy();
					this.putHistory(oe);
					if(client.getEntity().isEventTarget()){
						EventTarget svt = (EventTarget) client.getEntity();
						if(svt.checkerRegistered(OverlapChecker.instance())){
							OverlapEvent svEvent = new OverlapEvent(checkPrint, targetPrint, oe.getTouchPoint(), oe.getCrashNumber());
							svt.addEvent(svEvent);
							svt.putHistory(svEvent);
						}
					}
					return oe;
				}
				checkPrint.free();
			}
		}
		return null;
	}
	//Method used by OverlapChecker
	public Subject checkLastOverlap(){
		Event hEvent = getHistory(BaseEvent.touchEventCode);
		if(hEvent == null || !(hEvent instanceof OverlapEvent)){
			return null;
		}
		OverlapEvent oEvent = (OverlapEvent) hEvent;
		Imprint sv = oEvent.getSourcePrint();
		Subject check = (Subject) sv.getTarget();
		OverlapStrategy os = overlapStrategy.get(check.getEntity().sourceType());
		if(os == null || !os.hasToCheckOverlap(this, check)){
			return null;
		}
		if(check.getEntity().isEventTarget()){
			EventTarget checkT = (EventTarget) check.getEntity();
			Event lastCheckEvent = checkT.getHistory(BaseEvent.touchEventCode);
			if(lastCheckEvent == null || !(lastCheckEvent instanceof OverlapEvent)){
				this.removeHistory(BaseEvent.touchEventCode);
				return null;
			}
			if( ((OverlapEvent) lastCheckEvent).getCrashNumber() != oEvent.getCrashNumber() ){
				this.removeHistory(BaseEvent.touchEventCode);
				return null;
			}
		}
		Subject ts = (Subject) oEvent.getTargetPrint().getTarget();
		Validate.isTrue(this.equals(ts.getEntity()));
		Imprint checkView = check.getSafePrint();
		if(!os.isOverlap(ts.getInnerPrint(), checkView)){
			this.removeHistory(BaseEvent.touchEventCode);
			return null;
		}
		checkView.free();
		return check;
	}
	
	public boolean needCheckOverlap(Subject s) {
		OverlapStrategy os = overlapStrategy.get(s.getEntity().sourceType());
		if(os == null){
			return false;
		}
		return os.hasToCheckOverlap(this, s);
	}
	
//	public volatile int applyPrint = 0;
	@Override
	public void applyEngine(Engine engine){
		super.applyEngine(engine);
		World world = engine.getWorld();
//		this.adjustCurrentView(false);
		this.adjustCurrentPrint(false);
		for(Subject subject: getStaff()){
			District d = world.pointSector(subject.getCenter());
			Validate.isFalse(d == null, "Could not find district for point: " + subject.getCenter());
			subject.joinDistrict(d);
			subject.adjustCurrentPrint();
//			subject.update();
		}
		alive = true;
		Motor motor = engine.getLazyMotor();
		motor.addLiver(this);
		this.motor = motor;
	}
	
	public boolean alive(){
		return alive;
	}
	
	@Override
	public void outOfWorld(){
		alive = false;
		super.outOfWorld();
		boolean removed = this.motor.removeLiver(this);
		Validate.isTrue(removed);
	}

	public void startLog() {
		logger.startLog();
	}

	public void appendLog(String s) {
		logger.appendLog(s);
	}

	public void finishLog() {
		logger.finishLog();
	}

	public String getlog() {
		return logger.getlog();
	}

	public void log(String s){
		logger.log(s);
	}
	
	public void logStackTrace(String s){
		logger.logStackTrace(s);
	}
	
	public void clearLog(){
		logger.clearLog();
	}
}
