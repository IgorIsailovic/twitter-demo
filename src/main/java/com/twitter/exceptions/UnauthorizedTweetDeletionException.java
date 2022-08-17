package com.twitter.exceptions;

public class UnauthorizedTweetDeletionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public UnauthorizedTweetDeletionException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}