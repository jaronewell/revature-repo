package com.revature.dao;

import java.util.List;

import com.banking.Customer;

public interface CustomerDao {

	public void create(Customer c);
	
	public void update(Customer c);
	
	public List<Customer> readAll();
	
	public List<Customer> readAccountCustomers(int accountID);

	public List<Customer> readApplicationCustomers(int applicationID);
	
}
