package com.twitter.enums;

public enum BadRequestCodes {
	DEFAULT(100), METHOD_ARHUMENT_NOT_VALID(101), CONSTRAINT_VIOLATION(102);

	private final int value;

	BadRequestCodes(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
