package com.revature.project1.BusinessLogic;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
