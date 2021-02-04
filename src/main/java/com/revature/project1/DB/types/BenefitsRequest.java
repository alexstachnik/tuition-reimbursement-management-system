package com.revature.project1.DB.types;

import java.sql.Timestamp;

public class BenefitsRequest {
	private int requestID;
	private int employeeID;
	private Timestamp timestamp;
	
	private Timestamp eventTime;
	private String location;
	private String description;
	private double amount;
	private GradingFormat gradingFormat;
	private String minGrade;
	private EventType eventType;
	private String justification;
	private String workTimeMissed;
	private RequestStatus status;


	@Override
	public String toString() {
		return "BenefitsRequest [requestID=" + requestID + ", employeeID=" + employeeID + ", timestamp=" + timestamp
				+ ", eventTime=" + eventTime + ", location=" + location + ", description=" + description + ", amount="
				+ amount + ", gradingFormat=" + gradingFormat + ", minGrade=" + minGrade + ", eventType=" + eventType
				+ ", justification=" + justification + ", workTimeMissed=" + workTimeMissed + ", status=" + status
				+ "]";
	}

	public Timestamp getEventTime() {
		return eventTime;
	}

	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}

	public BenefitsRequest() {
		
	}
	
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public GradingFormat getGradingFormat() {
		return gradingFormat;
	}
	public void setGradingFormat(GradingFormat gradingFormat) {
		this.gradingFormat = gradingFormat;
	}
	public String getMinGrade() {
		return minGrade;
	}
	public void setMinGrade(String minGrade) {
		this.minGrade = minGrade;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getWorkTimeMissed() {
		return workTimeMissed;
	}
	public void setWorkTimeMissed(String workTimeMissed) {
		this.workTimeMissed = workTimeMissed;
	}
	public RequestStatus getStatus() {
		return status;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}

}
