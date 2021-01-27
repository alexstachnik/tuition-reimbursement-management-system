package com.revature.project1.Util;

public class TRMSException extends Exception {

	public TRMSException() {
		super();
	}
	
	public TRMSException(Throwable e) {
		super(e);
	}
	
	public TRMSException(String msg,Throwable e) {
		super(msg,e);
	}
	
	public TRMSException(String msg) {
		super(msg);
	}
	
}
