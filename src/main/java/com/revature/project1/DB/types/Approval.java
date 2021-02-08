package com.revature.project1.DB.types;

import java.sql.Timestamp;

public class Approval {

	private int approvalID;
	private int authID;
	private int requestID;
	private Timestamp approvalDate;
	private ApprovalType approvalType;
	private double approvalAmt;
	private int fileReference;
	public boolean isPreApproval;
	
	public int getAuthID() {
		return authID;
	}

	public void setAuthID(int authID) {
		this.authID = authID;
	}
	
	public double getApprovalAmt() {
		return approvalAmt;
	}

	public void setApprovalAmt(double approvalAmt) {
		this.approvalAmt = approvalAmt;
	}

	public boolean isPreApproval() {
		return isPreApproval;
	}

	public void setPreApproval(boolean isPreApproval) {
		this.isPreApproval = isPreApproval;
	}

	public Approval() {
		super();
		
	}
	
	public int getApprovalID() {
		return approvalID;
	}
	public void setApprovalID(int approvalID) {
		this.approvalID = approvalID;
	}
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public Timestamp getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}
	public ApprovalType getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(ApprovalType approvalType) {
		this.approvalType = approvalType;
	}

	public int getFileReference() {
		return fileReference;
	}

	public void setFileReference(int fileReference) {
		this.fileReference = fileReference;
	}

	@Override
	public String toString() {
		return "Approval [approvalID=" + approvalID + ", authID=" + authID + ", requestID=" + requestID
				+ ", approvalDate=" + approvalDate + ", approvalType=" + approvalType + ", approvalAmt=" + approvalAmt
				+ ", fileReference=" + fileReference + ", isPreApproval=" + isPreApproval + "]";
	}




	
}
