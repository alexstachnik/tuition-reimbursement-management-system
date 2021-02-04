package com.revature.project1.Util;

public class TRMSSQLException extends TRMSException {

	public TRMSSQLException() {
		super();
	}
	
	public TRMSSQLException(Throwable e) {
		super(e);
	}
	
	public TRMSSQLException(String msg,Throwable e) {
		super(msg,e);
	}
	
	public TRMSSQLException(String msg) {
		super(msg);
	}
	
}
