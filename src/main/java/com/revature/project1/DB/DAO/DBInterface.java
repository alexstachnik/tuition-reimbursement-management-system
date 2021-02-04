package com.revature.project1.DB.DAO;

import java.io.InputStream;
import java.util.List;

import com.revature.project1.DB.types.Approval;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
import com.revature.project1.Util.TRMSSQLException;

public interface DBInterface {
	
	// Indempotent
	void connect() throws TRMSSQLException;
	
	void disconnect() throws TRMSSQLException;
	
	Employee lookupEmployeeByName(String username) throws TRMSSQLException;
	
	int createRequest(BenefitsRequest request) throws TRMSSQLException;
	
	List<Integer> lookupAllRequestIDs() throws TRMSSQLException;
	
	BenefitsRequest lookupRequest(int requestID) throws TRMSSQLException ;
	
	int createEmployee(Employee employee) throws TRMSSQLException ;
	
	Employee lookupEmployee(int employeeID) throws TRMSSQLException ;
	
	void setManager(int employeeID, int managerID) throws TRMSSQLException ;
	
	//Return -1 if no direct supervisor
	int lookupManager(int employeeID) throws TRMSSQLException ;
	
	int lookupDepartmentHead(int employeeID) throws TRMSSQLException ;
	
	int addApproval(Approval approval) throws TRMSSQLException ;
	
	List<Approval> getApprovals(int requestID) throws TRMSSQLException ;
	
	int uploadAttachment(Attachment attachment, InputStream is) throws TRMSSQLException ;
	
	Attachment fetchAttachmentMetadata(int attachmentID) throws TRMSSQLException ;
	
	Attachment fetchAttachment(int attachmentID) throws TRMSSQLException ;
	
	void setGrade(int requestID, String grade) throws TRMSSQLException ;
	
	String lookupGrade(int requestID) throws TRMSSQLException ;
	
	void addPresentation(int requestID, int fileID) throws TRMSSQLException ;
	
	List<Integer> getPresentations(int requestID) throws TRMSSQLException;

}
