package com.twitter.enums;

public enum ForbiddenCodes {
	UNAUTHORIZED_TWEET_DELETION(0);
	
	private final int value;
	
	ForbiddenCodes(final int value){
		this.value=value;
	}

	public int getValue() {
		return value;
	}
	
	
	
}
