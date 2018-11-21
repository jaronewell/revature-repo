package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.Employee;
import com.banking.User.UserType;
import com.revature.util.ConnectionFactory;

public class EmployeeDaoPostgres implements EmployeeDao {

	private static Connection conn = ConnectionFactory.getConnectionFactory().createConnection();
	
	@Override
	public void create(Employee emp) {
		if (emp != null) {
			try {
				String sql = "insert into employee (username, password, isadmin) values ( ?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, emp.getUsername());
				stmt.setString(2, emp.getPassword());
				if(emp.getType().equals(UserType.Admin))
					stmt.setBoolean(3, true);
				else
					stmt.setBoolean(3, false);
				stmt.executeUpdate();

				sql = "select max(employeeid) from employee";
				stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					emp.setEmployeeID(rs.getInt(1));
				} else {
					throw new SQLException();
				}

			} catch (SQLException e) {
				System.out.println("Problem creating Employee");
				e.printStackTrace();
			}
		} else {
			System.out.println("The employee is null.");
			throw new IllegalArgumentException();
		}
	}

	@Override
	public List<Employee> readAll() {
		String sql = "select * from employee";
		List<Employee> employees = new ArrayList<Employee>();

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				if(rs.getBoolean(4))
					employee.setType(UserType.Admin);
				else
					employee.setType(UserType.Employee);
				
				employees.add(employee);

			}

			return employees;

		} catch (SQLException e) {
			System.out.println("Something went wrong when reading the accounts.");
			e.printStackTrace();
		}

		return new ArrayList<Employee>();
	}

}
