package com.revature.dao;

import java.util.List;

import com.revature.pojos.Employee;
import com.revature.pojos.Request;

public interface RequestDao {

	public void create(Request req);
	
	public List<Request> readAllForEmployee(Employee emp);

	public Request getRequestById(int requestId);

	public List<Request> readAll();
}
