package com.revature.dao;

import java.io.File;

import com.revature.pojos.Request;

public interface FileDao {

	public void makeFile(File file, Request req);
	
	public File readFile(int id);
	
	public File readAllForRequest(int reqId);
}
