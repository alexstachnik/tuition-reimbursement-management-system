package com.revature.project1.Util;

public class TRMSWebSafeException extends Exception {

	private int errorCode;
	
	public TRMSWebSafeException() {
		super();
		errorCode=500;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public TRMSWebSafeException(String message) {
		super(message);
		errorCode=500;
	}
	
	public TRMSWebSafeException(String message, int errorCode) {
		super(message);
		this.errorCode=errorCode;
	}

	public TRMSWebSafeException(Throwable cause) {
		super(cause);
		errorCode=500;
	}

	public TRMSWebSafeException(String message, Throwable cause) {
		super(message, cause);
		errorCode=500;
	}


}
