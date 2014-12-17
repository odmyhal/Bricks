package org.bricks.engine.tool;

import java.util.LinkedList;

public class Logger {

	private StringBuffer tmpLog;
	private LinkedList<String> log = new LinkedList<String>();
	
	public void startLog(){
		tmpLog = new StringBuffer(System.currentTimeMillis() + "--------------------------" + this);
	}
	
	public void appendLog(String s){
		tmpLog.append("\n" + s);
	}
	
	public void finishLog(){
		tmpLog.append("\n------------------------------------------");
		log.addFirst(tmpLog.toString());
		if(log.size() > 10){
			log.removeLast();
		}
	}
	
	public String getlog(){
		StringBuffer res = new StringBuffer();
		for(String lg : log){
			res.append(lg + "\n");
		}
		return res.toString();
	}
}
