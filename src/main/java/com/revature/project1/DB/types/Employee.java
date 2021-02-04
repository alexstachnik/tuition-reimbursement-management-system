package com.revature.project1.DB.types;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.iharder.Base64;

public class Employee {

	private int employeeID;
	private String name;
	private EmployeeRole role;
	private String passwordHash;
	private String password;
	private boolean superuser;
	
	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", name=" + name + ", role=" + role + ", passwordHash="
				+ passwordHash + ", password=" + password + ", isSuperuser=" + superuser + "]";
	}


	public void setSuperuser(boolean isSuperuser) {
		this.superuser = isSuperuser;
	}
	
	public boolean isSuperuser() {
		return this.superuser;
	}

	public Employee() {
		
	}

	public static String hash(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			return Base64.encodeBytes(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException();
		}
	}
		
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.passwordHash = Employee.hash(password);
	}



	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmployeeRole getRole() {
		return role;
	}

	public void setRole(EmployeeRole role) {
		this.role = role;
	}
	
	
	
}
