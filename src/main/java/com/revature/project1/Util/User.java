package com.revature.project1.Util;

public class User {
	
	private int employeeID;
	private String username;
	private boolean isSuperuser;
	private boolean isDeptHead;
	private boolean isBenco;
	
	public User() {
		
	}

	

	@Override
	public String toString() {
		return "User [employeeID=" + employeeID + ", username=" + username + ", isSuperuser=" + isSuperuser
				+ ", isDeptHead=" + isDeptHead + ", isBenco=" + isBenco + "]";
	}



	public int getEmployeeID() {
		return employeeID;
	}



	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isSuperuser() {
		return isSuperuser;
	}

	public void setSuperuser(boolean isSuperuser) {
		this.isSuperuser = isSuperuser;
	}

	public boolean isDeptHead() {
		return isDeptHead;
	}

	public void setDeptHead(boolean isDeptHead) {
		this.isDeptHead = isDeptHead;
	}

	public boolean isBenco() {
		return isBenco;
	}

	public void setBenco(boolean isBenco) {
		this.isBenco = isBenco;
	}
	
	

}
