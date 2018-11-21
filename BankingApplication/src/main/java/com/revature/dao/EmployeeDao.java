package com.revature.dao;

import java.util.List;

import com.banking.Employee;

public interface EmployeeDao {

	public void create(Employee e);
	
	public List<Employee> readAll();
}
