package com.revature.project1.Sessions;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.project1.BusinessLogic.ServiceController;
import com.revature.project1.DB.types.Approval;
import com.revature.project1.DB.types.ApprovalType;
import com.revature.project1.DB.types.Attachment;
import com.revature.project1.DB.types.BenefitsRequest;
import com.revature.project1.DB.types.Employee;
import com.revature.project1.DB.types.EventType;
import com.revature.project1.DB.types.GradingFormat;
import com.revature.project1.Util.TRMSException;
import com.revature.project1.Util.TRMSWebSafeException;
import com.revature.project1.Util.User;

public class SessionManager {
	
	private static final SessionManager globalSessionManager = new SessionManager();
	
	private Map<String,User> loggedInUsers;
	private Map<String,LocalDateTime> logInTimes;
	private ServiceController serviceManager;
	
	private SessionManager() {
		loggedInUsers = new HashMap<String,User>();
		serviceManager=new ServiceController();
		logInTimes = new HashMap<String,LocalDateTime>();
	}
	
	public Employee lookupEmployee(int employeeID) throws TRMSWebSafeException {
		try {
			return serviceManager.lookupEmployee(employeeID);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error looking up employee",500);
		}
	}
	
	public List<BenefitsRequest> getAllBenefitRequests() throws TRMSWebSafeException {
		try {
			return serviceManager.getAllBenefitRequests();
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error looking up benefit requests",500);
		}
	}
	
	public String getGrade(int requestID) {
		try {
			return serviceManager.getGrade(requestID);
		} catch (TRMSException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Attachment fetchAttachment(int fileID) throws TRMSWebSafeException {
		try {
			return serviceManager.fetchAttachment(fileID);
		} catch (TRMSException e) {
			throw new TRMSWebSafeException("Error fetching attachment",500);
		}
	}
	
	public List<String> getPresentations(int requestID) {
		try {
			System.out.println(requestID);
			List<Integer> attachments = serviceManager.getPresentations(requestID);
			List<String> retval=new ArrayList<String>();
			for (int i=0;i<attachments.size();++i) {
				retval.add("/project1/s/presentation.do?fileID="+attachments.get(i));
			}
			return retval;
		} catch (TRMSException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setGrade(int requestID,String grade) throws TRMSWebSafeException {
		try {
			serviceManager.setGrade(requestID, grade);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error adding grade",500);
		}
	}
	
	public void addPreapproval(Approval approval) throws TRMSWebSafeException {
		try {
			serviceManager.addPreapproval(approval);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error adding preapproval",500);
		}
	}
	
	public void addPresentation(int requestID, int fileID) throws TRMSWebSafeException {
		try {
			serviceManager.addPresentation(requestID, fileID);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error adding presentation",500);
		}
	}
	
	public int uploadAttachment(Attachment attachment, InputStream istream) throws TRMSWebSafeException {
		try {
			return serviceManager.uploadAttachment(attachment, istream);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error uploading attachment",500);
		}
	}
	
	public List<BenefitsRequest> getAllMyBenefitsRequests(String session) throws TRMSWebSafeException {
		try {
			User curUser = this.loggedInUsers.get(session);
			List<BenefitsRequest> brs = serviceManager.getAllBenefitRequests();
			List<BenefitsRequest> retval = new ArrayList<BenefitsRequest>();
			for (BenefitsRequest br:brs) {
				if (br.getEmployeeID()==curUser.getEmployeeID()) {
					retval.add(br);
				}
			}
			return retval;
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error looking up benefit requests",500);
		}
	}
	
	public void createBenefitsRequest(String session, String requestStr) throws TRMSWebSafeException {
		try {
			JSONObject json = new JSONObject(requestStr);
			BenefitsRequest br = new BenefitsRequest();
			
			String[] dateElts = json.getString("eventdate").split("-");
			if (dateElts.length != 3) {
				throw new TRMSWebSafeException("Error parsing date",400);
			}
			LocalDateTime eventDate;
			try {
				int year = Integer.parseInt(dateElts[0]);
				int month = Integer.parseInt(dateElts[1]);
				int day = Integer.parseInt(dateElts[2]);
				eventDate = LocalDateTime.of(year, month, day, 0, 0);
			} catch (NumberFormatException e) {
				throw new TRMSWebSafeException("Error parsing date",400);
			}
			Timestamp eventTimestamp = Timestamp.valueOf(eventDate);
			br.setEventTime(eventTimestamp);
			
			String location=json.getString("location");
			br.setLocation(location);
			
			String description=json.getString("description");
			br.setDescription(description);
			
			double amount=json.getDouble("amount");
			br.setAmount(amount);
			
			String gradingFormatStr=json.getString("grading-format");
			GradingFormat format;
			try {
				format=GradingFormat.valueOf(gradingFormatStr);
			} catch (IllegalArgumentException e) {
				throw new TRMSWebSafeException("Error parsing grading format",400);
			}
			br.setGradingFormat(format);
			
			String minGrade=json.getString("min-grade-input");
			br.setMinGrade(minGrade);
			
			String eventTypeStr=json.getString("event-type");
			EventType eventType;
			try {
				eventType=EventType.valueOf(eventTypeStr);
			} catch (IllegalArgumentException e) {
				throw new TRMSWebSafeException("Error parsing event type",400);
			}
			br.setEventType(eventType);
			
			String justification=json.getString("justification");
			br.setJustification(justification);
			
			String workTimeMissed=json.getString("work-time-missed");
			br.setWorkTimeMissed(workTimeMissed);
			
			User activeUser = this.loggedInUsers.get(session);
			this.serviceManager.addRequest(activeUser,br);
			System.out.println(br);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error parsing JSON",400);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error creating benefits request",500);
		}

	}
	
	public static SessionManager getSessionManager() {
		return globalSessionManager;
	}
	
	public boolean isLoggedIn(String sessionId) {
		return loggedInUsers.containsKey(sessionId);
	}
	
	public void closeRequest(int requestID) throws TRMSWebSafeException {
		try {
			serviceManager.closeRequest(requestID);
		} catch (TRMSException e) {
			throw new TRMSWebSafeException("Error closing request");
		}
	}
	
	public boolean employeeHasApproved(String sess, int requestID) throws TRMSWebSafeException {
		try {
			User curUser = this.loggedInUsers.get(sess);
			return serviceManager.employeeHasApproved(curUser.getEmployeeID(), requestID);
		} catch (TRMSException e) {
			throw new TRMSWebSafeException("Error checking if self approved");
		}
	}
	
	public boolean isBencoApproved(int requestID) throws TRMSWebSafeException {
		try {
			return serviceManager.isBencoApproved(requestID);
		} catch (TRMSException e) {
			throw new TRMSWebSafeException("Error checking if Benco approved");
		}
	}
	
	public boolean isBossApproved(int requestID) throws TRMSWebSafeException {
		try {
			return serviceManager.isBossApproved(requestID);
		} catch (TRMSException e) {
			throw new TRMSWebSafeException("Error checking if boss approved");
		}
	}
	
	public boolean isSuperuser(String sessionId) {
		User user = loggedInUsers.get(sessionId);
		
		if (user == null) {
			return false;
		} else {
			return user.isSuperuser();
		}
	}
	
	public boolean isBenco(String sessionId) {
		User user = loggedInUsers.get(sessionId);
		
		if (user == null) {
			return false;
		} else {
			return user.isBenco();
		}
	}
	
	public double totalReimbursement(int employeeID) throws TRMSWebSafeException {
		try {
			return serviceManager.totalReimbursement(employeeID);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error looking up reimbursements",500);
		}
	}
	
	public void addApproval(String sess, int requestID, ApprovalType approvalType, double amt) throws TRMSWebSafeException {
		Approval newApproval = new Approval();
		User curUser = loggedInUsers.get(sess);
		int authID = curUser.getEmployeeID();
		Timestamp approvalDate = Timestamp.valueOf(LocalDateTime.now());

		newApproval.setRequestID(requestID);
		newApproval.setApprovalType(approvalType);
		newApproval.setApprovalAmt(amt);
		newApproval.setAuthID(authID);
		newApproval.setApprovalDate(approvalDate);
		newApproval.setPreApproval(false);
		
		try {
			serviceManager.addApproval(newApproval);
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error adding approval",500);
		}
	}
	
	public BenefitsRequest lookupBenefitsRequest(int requestID) throws TRMSWebSafeException {
		try {
			BenefitsRequest request = serviceManager.lookupBenefitsRequest(requestID);
			return request;
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error retrieving benefits request",500);
		}
	}

	public boolean login(String sessionId, String username, String password) throws TRMSWebSafeException {
		try {
			User user = serviceManager.checkCredentials(username, password);
			if (user == null) {
				return false;
			} else {
				loggedInUsers.put(sessionId,user);
				logInTimes.put(sessionId, LocalDateTime.now());
				return true;
			}
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error checking credentials",500);
		}
	}
	
}
