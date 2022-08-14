package com.igor.igor.error;


public class ErrorResponse {
	
	private int httpCode;
	private int errorCode;
	private String message;
	
	public int getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorResponse(int httpCode, int errorCode, String message) {
		super();
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.message = message;
	}
	@Override
	public String toString() {
		return "ErrorResponse [httpCode=" + httpCode + ", errorCode=" + errorCode + ", message=" + message + "]";
	}
	
	
	
	

}
