package org.bricks.engine;

import org.bricks.utils.Quarantine;

public class SecuredMotor extends Motor{
	
	public static final Quarantine<Throwable> errors = new Quarantine<Throwable>(16);
	
	public void loop() {
		try{
			super.loop();
		}catch(Throwable e){
			stop();
			errors.push(e);
		}
	}
}
