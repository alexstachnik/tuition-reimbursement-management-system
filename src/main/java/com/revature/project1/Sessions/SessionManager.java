package com.revature.project1.Sessions;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.revature.project1.BusinessLogic.ServiceController;
import com.revature.project1.DB.types.BenefitsRequest;
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
	
	
	public List<BenefitsRequest> getAllBenefitRequests() throws TRMSWebSafeException {
		try {
			return serviceManager.getAllBenefitRequests();
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error looking up benefit requests");
		}
	}
	
	public void createBenefitsRequest(String session, String requestStr) throws TRMSWebSafeException {
		try {
			JSONObject json = new JSONObject(requestStr);
			BenefitsRequest br = new BenefitsRequest();
			
			String[] dateElts = json.getString("eventdate").split("-");
			if (dateElts.length != 3) {
				throw new TRMSWebSafeException("Error parsing date");
			}
			LocalDateTime eventDate;
			try {
				int year = Integer.parseInt(dateElts[0]);
				int month = Integer.parseInt(dateElts[1]);
				int day = Integer.parseInt(dateElts[2]);
				eventDate = LocalDateTime.of(year, month, day, 0, 0);
			} catch (NumberFormatException e) {
				throw new TRMSWebSafeException("Error parsing date");
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
				throw new TRMSWebSafeException("Error parsing grading format");
			}
			br.setGradingFormat(format);
			
			String minGrade=json.getString("min-grade-input");
			br.setMinGrade(minGrade);
			
			String eventTypeStr=json.getString("event-type");
			EventType eventType;
			try {
				eventType=EventType.valueOf(eventTypeStr);
			} catch (IllegalArgumentException e) {
				throw new TRMSWebSafeException("Error parsing event type");
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
			throw new TRMSWebSafeException("Internal error parsing JSON");
		} catch (TRMSException e) {
			e.printStackTrace();
			throw new TRMSWebSafeException("Error creating benefits request");
		}

	}
	
	public static SessionManager getSessionManager() {
		return globalSessionManager;
	}
	
	public boolean isLoggedIn(String sessionId) {
		return loggedInUsers.containsKey(sessionId);
	}
	
	public boolean isSuperuser(String sessionId) {
		User user = loggedInUsers.get(sessionId);
		
		if (user == null) {
			return false;
		} else {
			return user.isSuperuser();
		}
	}

	public boolean login(String sessionId, String username, String password) throws TRMSException {
		User user = serviceManager.checkCredentials(username, password);

		if (user == null) {
			return false;
		} else {
			loggedInUsers.put(sessionId,user);
			logInTimes.put(sessionId, LocalDateTime.now());
			return true;
		}
	}
	
}
