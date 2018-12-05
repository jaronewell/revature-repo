package com.revature.pojos;

import com.revature.service.EmployeeService;

public class Employee {

	private static final double TOTAL_REIMBURSEMENT = 1000;
	
	public enum EmployeeType{
		Employee(0),
		Supervisor(1),
		DepartmentHead(2),
		BenCo(3);
		
		public final int value;
		private EmployeeType(int value) { this.value = value; }
		
		public int getValue() {
			return this.value;
		}
	}
	
	private int employeeId;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private int supervisorId;
	private int departmentHeadId;
	private double pendingReimbursements;
	private double awardedReimbursements;
	private EmployeeType type;
	
	private EmployeeService empService = new EmployeeService();
	
	public double getAwardedReimbursements() {
		return awardedReimbursements;
	}
	public void setAwardedReimbursements(double awardedReimbursements) {
		this.awardedReimbursements = awardedReimbursements;
	}
	public boolean exceedsAvailableFunds() {
		if(awardedReimbursements + pendingReimbursements > TOTAL_REIMBURSEMENT){
			return true;
		}
		else {
			return false;
		}
	}
	public Employee(int employeeId, String firstName, String lastName, String username, String password,
			int supervisorId, int departmentHeadId, EmployeeType type) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.supervisorId = supervisorId;
		this.departmentHeadId = departmentHeadId;
		this.pendingReimbursements = empService.getPendingReimbursements(this);
		this.awardedReimbursements = empService.getAwardedReimbursements(this);
		this.setType(type);
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}
	public int getDepartmentHeadId() {
		return departmentHeadId;
	}
	public void setDepartmentHeadId(int departmentHeadId) {
		this.departmentHeadId = departmentHeadId;
	}
	public double getPendingReimbursements() {
		return pendingReimbursements;
	}
	public void setPendingReimbursements(double pendingReimbursements) {
		this.pendingReimbursements = pendingReimbursements;
	}
	public EmployeeType getType() {
		return type;
	}
	public void setType(EmployeeType type) {
		this.type = type;
	}
	
	
}
