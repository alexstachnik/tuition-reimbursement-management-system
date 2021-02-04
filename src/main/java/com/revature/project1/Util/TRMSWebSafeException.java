package com.revature.project1.Util;

public class TRMSWebSafeException extends Exception {

	public TRMSWebSafeException() {
	}

	public TRMSWebSafeException(String message) {
		super(message);
	}

	public TRMSWebSafeException(Throwable cause) {
		super(cause);
	}

	public TRMSWebSafeException(String message, Throwable cause) {
		super(message, cause);
	}


}
