package com.twitter.enums;

public enum BadRequestCodes {
	DEFAULT(0),
	METHOD_ARHUMENT_NOT_VALID(1),
	CONSTRAINT_VIOLATION(2);
	
	private final int value;
	
	BadRequestCodes(final int value){
		this.value=value;
	}

	public int getValue() {
		return value;
	}
	
	
	
}
