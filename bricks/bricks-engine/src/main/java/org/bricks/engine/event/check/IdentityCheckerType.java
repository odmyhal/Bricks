package org.bricks.engine.event.check;

public class IdentityCheckerType extends CheckerType{
	
	private Object identity;
	
	public IdentityCheckerType(CheckerType prototype){
		super(prototype);
	}

	public IdentityCheckerType(CheckerType prototype, Object identity) {
		super(prototype);
		this.identity = identity;
	}

	public boolean match(CheckerType chType){
		if( super.match(chType) && (chType instanceof IdentityCheckerType) ){
			return identity.equals(((IdentityCheckerType)chType).identity);
		}
		return false;
	}
	
	public void setIdentity(Object identity){
		this.identity = identity;
	}
}
