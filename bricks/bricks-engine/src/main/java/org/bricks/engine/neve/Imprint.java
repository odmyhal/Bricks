package org.bricks.engine.neve;

public interface Imprint<P extends Printable> {
	
	//should be protected without interface
//	public void checkCounterZero();

	//should be protected and abstract in base without interface
	public void init();
	
	/*
	 * Print can't be occupied after undUse method called(counter == 0 in this case)
	 */
	public boolean occupy();
	
	public void intStoreInitialization();
	
	//withoud interface should be prtected final
//	public void occupyFirst();
	
	public void free();
	
//	protected void endUse();
	
	public P getTarget();
}
