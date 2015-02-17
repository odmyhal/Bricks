package org.bricks.engine.staff;

public interface Logable {
	
	public void startLog();
	public void appendLog(String s);
	public void finishLog();
	public void log(String s);
	public void logStackTrace(String s);
	public String getlog();
	
}
