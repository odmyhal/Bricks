package org.bricks.engine.tool;

import java.util.LinkedList;

public class Logger {

	private StringBuffer tmpLog;
	private LinkedList<String> log = new LinkedList<String>();
	private static final int logSize = 30;
	
	public synchronized void startLog(){
		tmpLog = new StringBuffer(System.currentTimeMillis() + "-----" + this + "-----thread:------" + Thread.currentThread().getName());
	}
	
	public synchronized void appendLog(String s){
		tmpLog.append("\n" + s);
	}
	
	public synchronized void finishLog(){
		tmpLog.append("\n------------------------------------------");
		log.addLast(tmpLog.toString());
		while(log.size() > logSize){
			log.removeFirst();
		}
	}
	
	public synchronized void log(String s){
		startLog();
		appendLog(s);
		finishLog();
	}
	
	public void logStackTrace(String msg){
		StringBuffer buf = new StringBuffer(msg + "\n");
		for(StackTraceElement stm : Thread.currentThread().getStackTrace()){
			buf.append("	" + stm.getFileName() + " - " + stm.getMethodName() + " - " + stm.getLineNumber() + "\n");
		}
		log(buf.toString());
	}
	
	public synchronized String getlog(){
		StringBuffer res = new StringBuffer();
		for(String lg : log){
			res.append(lg + "\n");
		}
		return res.toString();
	}
	
	public synchronized void clearLog(){
		log.clear();
	}
}
