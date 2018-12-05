package com.revature.dao;

import java.util.List;

import com.revature.pojos.Employee;

public interface EmployeeDao {

	public void create(Employee emp);
	
	public List<Employee> readAll();

	public Employee getEmployeeByUsername(String username);
	
	public Employee getSupervisorByEmployee(Employee emp);

	public Employee getDepartmentHeadByEmployee(Employee emp);
}
