package com.revature.service;

import com.revature.dao.EmployeeDaoPostgres;
import com.revature.dao.EmployeeDao;
import com.revature.pojos.Employee;

public class AuthService {

	EmployeeDao edao = new EmployeeDaoPostgres();
	
	public Employee validateEmployee(String username, String password) {
		
		Employee emp = edao.getEmployeeByUsername(username);
		
		if(emp == null || !emp.getPassword().equals(password))
			return null;
				
		return emp;
	}
}
