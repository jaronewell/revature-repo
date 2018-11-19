package com.revature.dao;

import java.util.List;

import com.banking.Application;

public interface ApplicationDao {

	public void create(Application a);
	
	public void update(Application a);
	
	public List<Application> readAll();
}
