package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.revature.util.ConnectionFactory;

public class LogDaoPostgres implements LogDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
			
	@Override
	public void create(String message) {
		try {
			String sql = "Insert into log (time, message) values (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
			stmt.setString(2, message);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Problem adding to log");
			e.printStackTrace();
		}
	}

}
