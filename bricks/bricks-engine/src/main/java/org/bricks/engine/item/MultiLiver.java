package org.bricks.engine.item;

import java.util.Map;

import org.bricks.exception.Validate;
import org.bricks.engine.Engine;
import org.bricks.engine.Motor;
import org.bricks.engine.event.BaseEvent;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.EventTarget;
import org.bricks.engine.event.OverlapEvent;
import org.bricks.engine.event.PrintOverlapEvent;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.event.handler.EventHandlerManager;
import org.bricks.engine.event.overlap.OSRegister;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.neve.EntityPrint;
import org.bricks.engine.neve.Imprint;
import org.bricks.engine.staff.AvareTimer;
import org.bricks.engine.staff.Habitant;
import org.bricks.engine.staff.Satellite;
import org.bricks.engine.staff.Subject;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.tool.Live;
import org.bricks.engine.tool.Logger;
import org.bricks.utils.Cache;
import org.bricks.utils.LinkLoop;

public abstract class MultiLiver<S extends Subject, P extends EntityPrint, C> 
	extends MultiSubjectEntity<S, P, C> 
	implements Liver<P>{
	
	private Live live;
	private Logger logger = new Logger();
	private Map<String, OverlapStrategy> overlapStrategy;
	private Motor motor;
	private boolean alive = false;
	private boolean needUpdate = true;

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
	public void unregisterEventChecker(EventChecker checker){
		live.unregisterEventChecker(checker);
	}
	public void addEvent(Event e){
		live.addEvent(e);
	}

	public void refreshCheckers(long currentTime){
		live.refreshCheckers(currentTime);
	}
	
	public boolean hasChekers(){
		return live.hasChekers();
	}
	
	/**
	 * Call ONLY in motor thread
	 * @param time
	 */
	public void timerSet(long time){
		live.timerSet(time);
	}
	
	/**
	 * Call ONLY in motor thread
	 * @param time
	 */
	public void timerAdd(long time){
		live.timerAdd(time);
	}

	public boolean isEventTarget(){
		return Boolean.TRUE;
	}

	public Event popEvent() {
		return live.popEvent();
	}
/*	
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
*/	
	protected final void adjustInMotorPrint(){
		this.adjustCurrentPrint(false);
		for(int i = 0; i < satellites.size(); i++){
			satellites.get(i).update();
		}
	}
	
	protected void innerProcess(long currentTime){
		//Method rewrite by MultiRoller and MultiWalker
	};
	
	//Need to be called in motor thread
	public void setUpdate(){
		needUpdate = true;
	}
	
	protected final boolean needUpdate(){
		return needUpdate;
	}
	
	public final void motorProcess(long currentTime){
		processEvents(currentTime);
		if(alive){
			innerProcess(currentTime);
			if(needUpdate){
				adjustInMotorPrint();
				needUpdate = false;
			}
		}
	}
	
	public void processEvents(long currentTime){
		refreshCheckers(currentTime);
		if(hasChekers()){
			for(EventChecker checker : live){
				if(checker.isActive()){
					checker.checkEvents(this, currentTime);
				}
			}
		}
	}
	
	public OverlapEvent checkOverlap(Subject mySubject, Habitant client){
		OverlapStrategy os = overlapStrategy.get(client.getEntity().sourceType());
		if(os == null){
			return null;
		}
		if(os.hasToCheckOverlap(this, client)){
			Validate.isTrue(mySubject.getEntity() == this, "Alloved to check only inner subjects");
			//Has to be synchronized with client popEvetn
			synchronized(client.getEntity()){
				Imprint targetPrint = mySubject.getSafePrint();
//				Imprint checkPrint = client.getSafePrint();
				OverlapEvent oe = os.checkForOverlapEvent(targetPrint, client);
				if(oe != null){
					targetPrint.occupy();
//					this.putHistory(oe);
					if(client.getEntity() instanceof EventTarget){
						EventTarget svt = (EventTarget) client.getEntity();
						if(EventHandlerManager.containsHandler(svt, PrintOverlapEvent.class, this.sourceType())){
//							PrintOverlapEvent svEvent = new PrintOverlapEvent(checkPrint, targetPrint, oe.getTouchPoint(), oe.getCrashNumber());
							OverlapEvent svEvent = os.produceRevertEvent(oe);
							svt.addEvent(svEvent);
//							svt.putHistory(svEvent);
						}
					}
					return oe;
				}
				targetPrint.free();
//				checkPrint.free();
			}
		}
		return null;
	}
	//Method used by OverlapChecker
/*	public Subject checkLastOverlap(){
		Event hEvent = getHistory(BaseEvent.touchEventCode);
		if(hEvent == null || !(hEvent instanceof PrintOverlapEvent)){
			return null;
		}
		PrintOverlapEvent oEvent = (PrintOverlapEvent) hEvent;
		Imprint sv = oEvent.getSourcePrint();
		Subject check = (Subject) sv.getTarget();
		OverlapStrategy os = overlapStrategy.get(check.getEntity().sourceType());
		if(os == null || !os.hasToCheckOverlap(this, check)){
			return null;
		}
		if(check.getEntity() instanceof EventTarget){
			EventTarget checkT = (EventTarget) check.getEntity();
			Event lastCheckEvent = checkT.getHistory(BaseEvent.touchEventCode);
			if(lastCheckEvent == null || !(lastCheckEvent instanceof PrintOverlapEvent)){
				this.removeHistory(BaseEvent.touchEventCode);
				return null;
			}
			if( ((PrintOverlapEvent) lastCheckEvent).getCrashNumber() != oEvent.getCrashNumber() ){
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
	}*/
/*	
	public boolean needCheckOverlap(Subject s) {
		OverlapStrategy os = overlapStrategy.get(s.getEntity().sourceType());
		if(os == null){
			return false;
		}
		return os.hasToCheckOverlap(this, s);
	}
*/	
//	public volatile int applyPrint = 0;
	@Override
	public final void applyEngine(Engine engine){
		super.applyEngine(engine);
		World world = engine.getWorld();
//		this.adjustCurrentView(false);
		this.adjustCurrentPrint(false);
		for(int i = 0; i < staff.size(); i++){
			staff.get(i).joinWorld(world);
/*			District d = world.pointSector(subject.getCenter());
			Validate.isFalse(d == null, "Could not find district for point: " + subject.getCenter());
			subject.joinDistrict(d);
			subject.adjustCurrentPrint();*/
			//subject.update()
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
	public void disappear(){
		alive = false;
		super.disappear();
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
