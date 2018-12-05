package com.revature.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

	private static final String filename = "C:/Users/jaron/Documents/workspace-sts-3.9.4.RELEASE/ReimbursementApplication/src/main/resources/datasource.properties";
	
	private static String url;
	
	private static String user;
	
	private static String password;
	
	private static ConnectionFactory cf = null;
	
	private ConnectionFactory() {
		
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filename);
			prop.load(fis);
			url = prop.getProperty("url");
			user = prop.getProperty("username");
			password = prop.getProperty("password");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized static ConnectionFactory getConnectionFactory() {
		if(cf == null) {
			cf = new ConnectionFactory();
		}
		return cf;
	}
	
	public Connection createConnection() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		try {
		conn = DriverManager.getConnection(url, user, password);
		}catch(SQLException e) {
			System.out.println("Unable to create connection to database.");
			e.printStackTrace();
		}
		return conn;
	}
}
