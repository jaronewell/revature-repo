package com.revature.pojos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Request {

	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", employee=" + employee + ", requestDate=" + requestDate
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", location=" + location + ", description="
				+ description + ", cost=" + cost + ", format=" + format + ", type=" + type + ", justification="
				+ justification + ", status=" + status + ", comments=" + comments + ", isUrgent=" + isUrgent
				+ ", expectedAmount=" + expectedAmount + ", awardedAmount=" + awardedAmount + "]";
	}

	public enum GradingFormat{
		Graded(1),
		Presentation(2);
		
		public final int value;
		private GradingFormat(int value) { this.value = value; }
		
		public int getValue() {
			return this.value;
		}
	}
	
	public enum EventType{
		UniversityCourse(1),
		Seminar(2),
		CertificationPreparationClass(3),
		Certification(4),
		TechnicalTraining(5),
		Other(6);
		
		public final int value;
		private EventType(int value) { this.value = value; }
		
		public int getValue() {
			return this.value;
		}
	}
	
	private static final Map<EventType, Double> eventTypes;
	static{
		eventTypes = new HashMap<EventType, Double>();
		eventTypes.put(EventType.UniversityCourse, 0.8);
		eventTypes.put(EventType.Seminar, 0.6);
		eventTypes.put(EventType.CertificationPreparationClass, 0.75);
		eventTypes.put(EventType.Certification, 1.0);
		eventTypes.put(EventType.TechnicalTraining, 0.9);
		eventTypes.put(EventType.Other, 0.3);
	}
	
	private int requestId;
	  
	private Employee employee;
	
	private LocalDate requestDate;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String location;
	
	private String description;
	
	private double cost;
	
	private GradingFormat format;
	
	private EventType type;
	
	private String justification;
	
	private int status;
	
	private String comments;
	
	private boolean isUrgent;
	
	private double expectedAmount;
	
	private double awardedAmount;
	
	private int missedHours;

	public Request() {
		
	}
	
	public Request(int requestId, Employee employee, LocalDate requestDate, LocalDate startDate, LocalDate endDate,
			String location, String description, double cost, GradingFormat format, EventType type,
			String justification, int status, String comments, int awardedAmount, int missedHours) {
		super();
		this.requestId = requestId;
		this.employee = employee;
		this.requestDate = requestDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.description = description;
		this.cost = cost;
		this.format = format;
		this.type = type;
		this.justification = justification;
		this.status = status;
		this.comments = comments;
		this.awardedAmount = awardedAmount;
		this.missedHours = missedHours;
		
		if(ChronoUnit.DAYS.between(startDate, endDate) <= 14) {
			this.isUrgent = true;
		}
		else {
			this.isUrgent = false;
		}
		
		this.expectedAmount = eventTypes.get(type) * cost;
	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee emp) {
		this.employee = employee;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public GradingFormat getFormat() {
		return format;
	}

	public void setFormat(GradingFormat format) {
		this.format = format;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public double getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(double expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	public double getAwardedAmount() {
		return awardedAmount;
	}

	public void setAwardedAmount(double awardedAmount) {
		this.awardedAmount = awardedAmount;
	}

	public int getMissedHours() {
		return missedHours;
	}

	public void setMissedHours(int missedHours) {
		this.missedHours = missedHours;
	}
}
