package com.revature.project1.BusinessLogic;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.project1.DB.DAO.DBInterface;
import com.revature.project1.DB.DAO.SQL_DB;
import com.revature.project1.DB.types.Approval;
import com.revature.project1.DB.types.ApprovalType;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
import com.revature.project1.DB.types.EmployeeRole;
import com.revature.project1.DB.types.RequestStatus;
import com.revature.project1.Util.TRMSException;
import com.revature.project1.Util.User;

public class ServiceController {
	
	private DBInterface db;
	
	public ServiceController() {
		this.db=new SQL_DB();
	}
	
	public BenefitsRequest addRequest(User user, BenefitsRequest br) throws TRMSException {
		br.setEmployeeID(user.getEmployeeID());
		br.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
		br.setStatus(RequestStatus.OPEN);
		
		db.connect();
		int requestID=db.createRequest(br);
		br.setRequestID(requestID);
		return br;
	}
	
	public List<BenefitsRequest> getAllBenefitRequests() throws TRMSException {
		db.connect();
		List<Integer> ids = db.lookupAllRequestIDs();
		List<BenefitsRequest> retval = new ArrayList<BenefitsRequest>();
		for (int i=0;i<ids.size();++i) {
			retval.add(db.lookupRequest(ids.get(i)));
		}
		return retval;
	}
	
	public int uploadAttachment(Attachment attachment, InputStream istream) throws TRMSException {
		db.connect();
		return db.uploadAttachment(attachment, istream);
	}
	
	public Attachment fetchAttachment(int attachmentID) throws TRMSException {
		db.connect();
		return db.fetchAttachment(attachmentID);
	}
	
	public List<Integer> getPresentations(int requestID) throws TRMSException {
		db.connect();
		return db.getPresentations(requestID);
	}
	
	public void setGrade(int requestID, String grade) throws TRMSException {
		db.connect();
		db.setGrade(requestID, grade);
	}
	
	public void closeRequest(int requestID) throws TRMSException {
		db.connect();
		db.closeRequest(requestID);
	}
	
	public String getGrade(int requestID) throws TRMSException {
		db.connect();
		return db.lookupGrade(requestID);
	}
	
	public void addPresentation(int requestID, int fileID) throws TRMSException {
		db.connect();
		db.addPresentation(requestID, fileID);
	}
	
	public void addPreapproval(Approval approval) throws TRMSException {
		db.connect();
		db.addApproval(approval);
	}
	
	public double totalReimbursement(int employeeID) throws TRMSException {
		db.connect();
		double sum=0.0;
		List<Integer> requestIDs=db.lookupAllRequestsByEmployee(employeeID);
		for (Integer id:requestIDs) {
			BenefitsRequest request = db.lookupRequest(id);
			sum += request.getAmount();
		}
		return sum;
	}
	
	public boolean employeeHasApproved(int employeeID, int requestID) throws TRMSException {
		db.connect();
		List<Approval> approvals=db.getApprovals(requestID);
		for (Approval approval : approvals) {
			if (	(approval.getAuthID() == employeeID) &&
					(approval.getApprovalType() == ApprovalType.APPROVE)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBencoApproved(int requestID) throws TRMSException {
		db.connect();
		List<Approval> approvals=db.getApprovals(requestID);
		for (Approval approval : approvals) {
			if (approval.getApprovalType() == ApprovalType.APPROVE) {
				Employee authEmployee = db.lookupEmployee(approval.getAuthID());
				if (	(authEmployee.getRole() == EmployeeRole.BENCO) ||
						(authEmployee.getRole() == EmployeeRole.BENCO_AND_DEPT_HEAD)) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	public boolean isBossApproved(int requestID) throws TRMSException {
		db.connect();
		BenefitsRequest br=db.lookupRequest(requestID);
		int employeeID=br.getEmployeeID();
		int bossID = db.lookupManager(employeeID);
		
		List<Approval> approvals=db.getApprovals(requestID);
		for (Approval approval : approvals) {
			if (approval.getApprovalType() == ApprovalType.APPROVE) {
				Employee authEmployee = db.lookupEmployee(approval.getAuthID());
				if (authEmployee.getRole() == EmployeeRole.DEPT_HEAD) {
					return true;
				}
				if (authEmployee.getEmployeeID() == bossID) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void addApproval(Approval approval) throws TRMSException {
		db.connect();
		db.addApproval(approval);
	}
	
	public BenefitsRequest lookupBenefitsRequest(int requestID) throws TRMSException {
		db.connect();
		return db.lookupRequest(requestID);
	}

	public Employee lookupEmployee(int employeeID) throws TRMSException {
		db.connect();
		return db.lookupEmployee(employeeID);
	}
	
	public User checkCredentials(String username, String password) throws TRMSException {
		db.connect();
		String passwordHash = Employee.hash(password);
		
		Employee employee = db.lookupEmployeeByName(username);
		
		if (employee == null) {
			return null;
		}
		if (employee.getPasswordHash().equals(passwordHash)) {
			User retval = new User();
			retval.setUsername(username);
			retval.setSuperuser(employee.isSuperuser());
			retval.setEmployeeID(employee.getEmployeeID());
			switch (employee.getRole()) {
			case BENCO:
				retval.setBenco(true);
				retval.setDeptHead(false);
				break;
			case BENCO_AND_DEPT_HEAD:
				retval.setBenco(true);
				retval.setDeptHead(true);
				break;
			case DEPT_HEAD:
				retval.setBenco(false);
				retval.setDeptHead(true);
				break;
			case NORMAL:
				break;
			}
			return retval;
		} else {
			return null;
		}
	}

}
