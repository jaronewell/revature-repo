package com.revature.service;

import java.util.List;

import com.revature.dao.EmployeeDao;
import com.revature.dao.EmployeeDaoPostgres;
import com.revature.dao.RequestDao;
import com.revature.dao.RequestDaoPostgres;
import com.revature.pojos.Employee;
import com.revature.pojos.Request;

public class EmployeeService {

	private EmployeeDao eDao = new EmployeeDaoPostgres();
	private RequestDao rDao = new RequestDaoPostgres();
	
	public List<Employee> readAllEmployees(){
		return eDao.readAll();
	}
	
	public Employee readEmployee(String username) {
		return eDao.getEmployeeByUsername(username);
	}
	
	public double getPendingReimbursements(Employee e) {
		List<Request> requestList = rDao.readAllForEmployee(e);
		
		double pendingReimbursements = 0;
		
		if(requestList.isEmpty()) {
			return pendingReimbursements;
		}
		
		for(Request req : requestList) {
			if(req.getStatus() >= 0 && req.getStatus() <= 3) {
				pendingReimbursements += req.getExpectedAmount();
			}
		}
		
		return pendingReimbursements;
	}
	
	public double getAwardedReimbursements(Employee e) {
		List<Request> requestList = rDao.readAllForEmployee(e);
		
		double awardedReimbursements = 0;
		
		for(Request req : requestList) {
			if(req.getStatus() == 4) {
				awardedReimbursements += req.getAwardedAmount();
			}
		}
		
		return awardedReimbursements;
	}
}
