package com.revature.dao;

import java.util.List;

import com.banking.BankAccount;

//DAO is used to handle CRUD operations with the database
public interface BankAccountDao {
	
	public void create(BankAccount b);
	
	public void deactivate(BankAccount b);
	
	public void update(BankAccount b);
	
	public BankAccount read(int id);
	
	public List<BankAccount> readAll();
}
