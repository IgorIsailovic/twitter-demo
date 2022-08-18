package com.twitter.enums;

public enum NotFoundCodes {
	TWEET_NOT_FOUND(100);

	private final int value;

	NotFoundCodes(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
