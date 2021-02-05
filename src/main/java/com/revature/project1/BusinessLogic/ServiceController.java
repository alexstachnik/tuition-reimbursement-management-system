package com.revature.project1.BusinessLogic;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.project1.DB.DAO.DBInterface;
import com.revature.project1.DB.DAO.SQL_DB;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
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
