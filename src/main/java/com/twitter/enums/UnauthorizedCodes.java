package com.twitter.enums;

public enum UnauthorizedCodes {
	MISSING_REQUEST_HEADER(0);
	
	
	private final int value;
	
	UnauthorizedCodes(final int value){
		this.value=value;
	}

	public int getValue() {
		return value;
	}
	
	
	
}
