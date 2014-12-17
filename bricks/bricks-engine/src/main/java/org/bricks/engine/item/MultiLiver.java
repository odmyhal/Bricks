package org.bricks.engine.item;

import java.util.Collection;
import java.util.Map;

import org.bricks.exception.Validate;
import org.bricks.engine.Engine;
import org.bricks.engine.Motor;
import org.bricks.engine.event.Event;
import org.bricks.engine.event.check.EventChecker;
import org.bricks.engine.event.overlap.OverlapStrategy;
import org.bricks.engine.pool.District;
import org.bricks.engine.pool.Subject;
import org.bricks.engine.pool.World;
import org.bricks.engine.staff.Liver;
import org.bricks.engine.tool.Live;
import org.bricks.engine.tool.Logger;

public abstract class MultiLiver<S extends Subject> extends MultiSubjectEntity<S> implements Liver{
	
	private Live live = new Live();
	private Logger logger = new Logger();
	protected Map<String, OverlapStrategy> overlapStrategy;
	private Motor motor;
	private boolean alive = false;

	protected MultiLiver() {
		overlapStrategy = initOverlapStrategy();
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
	public void refreshCheckers(){
		live.refreshCheckers();
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
		processEvents(currentTime);
	}
	
	public void processEvents(long currentTime){
		refreshCheckers();
		if(hasChekers()){
			for(EventChecker checker : getCheckers()){
				checker.checkEvents(this, currentTime);
			}
		}
	}
	
	public boolean needCheckOverlap(Subject s) {
		OverlapStrategy os = overlapStrategy.get(s.getEntity().sourceType());
		if(os == null){
			return false;
		}
		return os.hasToCheckOverlap(s);
	}
	
	@Override
	public void applyEngine(Engine engine){
		super.applyEngine(engine);
		World world = engine.getWorld();
		this.adjustCurrentView(false);
		for(Subject subject: getStaff()){
			District d = world.pointSector(subject.getCenter());
//			d.lockView();
			subject.joinDistrict(d);
//			d.unlockView();
			subject.adjustCurrentView();
		}
		alive = true;
		Motor motor = engine.getLazyMotor();
		motor.addSubject(this);
		this.motor = motor;
	}
	
	public boolean alive(){
		return alive;
	}
	
	@Override
	public void outOfWorld(){
		alive = false;
		super.outOfWorld();
		boolean removed = this.motor.removeSubject(this);
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

}
